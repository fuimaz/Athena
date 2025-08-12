package com.fuimaz.athena.security;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;

/**
 * A MyBatis interceptor that appends data scope conditions to SQL dynamically.
 * It inspects the mapper method for {@link DataScope} to determine the column and alias,
 * then reads scope ids from {@link DataScopeContext}.
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DataScopeInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(DataScopeInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        // Unwrap to RoutingStatementHandler -> delegate
        while (metaObject.hasGetter("h")) {
            Object object = metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(object);
        }
        if (metaObject.hasGetter("target")) {
            Object object = metaObject.getValue("target");
            metaObject = SystemMetaObject.forObject(object);
        }
        if (!(metaObject.getOriginalObject() instanceof RoutingStatementHandler)) {
            return invocation.proceed();
        }

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        String id = mappedStatement.getId();

        DataScope dataScope = resolveDataScopeAnnotation(id);
        if (dataScope == null) {
            return invocation.proceed();
        }

        if (dataScope.ignoreForSuperAdmin() && DataScopeContext.isSuperAdmin()) {
            return invocation.proceed();
        }

        Set<Long> scopeIds = DataScopeContext.getScopeIds();
        if (scopeIds == null || scopeIds.isEmpty()) {
            // If no scope ids, make it return empty when data scope is required
            String emptyCondition = buildAlwaysFalseCondition(dataScope);
            String newSql = appendCondition(originalSql, emptyCondition);
            setSql(metaObject, newSql);
            return invocation.proceed();
        }

        String condition = buildInCondition(dataScope, scopeIds);
        String newSql = appendCondition(originalSql, condition);
        setSql(metaObject, newSql);
        if (log.isDebugEnabled()) {
            log.debug("DataScope applied on [{}], sql: {}", id, newSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // no-op
    }

    private void setSql(MetaObject metaObject, String sql) {
        metaObject.setValue("delegate.boundSql.sql", sql);
    }

    private String buildColumnReference(DataScope dataScope) {
        String alias = dataScope.alias();
        String column = dataScope.column();
        if (alias != null && alias.trim().length() > 0) {
            return alias + "." + column;
        }
        return column;
    }

    private String buildAlwaysFalseCondition(DataScope dataScope) {
        return buildColumnReference(dataScope) + " IN (NULL)";
    }

    private String buildInCondition(DataScope dataScope, Collection<Long> ids) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Long id : ids) {
            joiner.add(String.valueOf(id));
        }
        return buildColumnReference(dataScope) + " IN (" + joiner + ")";
    }

    private String appendCondition(String sql, String condition) {
        String trimmed = sql.trim();
        String upper = trimmed.toUpperCase();
        if (upper.startsWith("SELECT") || upper.startsWith("DELETE") || upper.startsWith("UPDATE")) {
            // naive WHERE appender that handles presence/absence of WHERE
            if (upper.contains(" WHERE ")) {
                return trimmed + " AND " + condition;
            } else {
                return trimmed + " WHERE " + condition;
            }
        }
        return trimmed;
    }

    private DataScope resolveDataScopeAnnotation(String mappedStatementId) {
        // mappedStatementId is fully qualified method id: com.pkg.Mapper.method
        int lastDot = mappedStatementId.lastIndexOf('.');
        if (lastDot <= 0 || lastDot >= mappedStatementId.length() - 1) {
            return null;
        }
        String className = mappedStatementId.substring(0, lastDot);
        String methodName = mappedStatementId.substring(lastDot + 1);
        try {
            Class<?> mapperClass = Class.forName(className);
            // Try method-level first
            Method target = findMethodByName(mapperClass, methodName);
            if (target != null) {
                DataScope methodAnno = target.getAnnotation(DataScope.class);
                if (methodAnno != null) {
                    return methodAnno;
                }
            }
            // Then type-level
            return mapperClass.getAnnotation(DataScope.class);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Method findMethodByName(Class<?> clazz, String methodName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}