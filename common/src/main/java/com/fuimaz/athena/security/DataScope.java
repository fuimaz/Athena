package com.fuimaz.athena.security;

import java.lang.annotation.*;

/**
 * Marks a mapper method or service method to apply data scope filtering.
 * You can specify the column name(s) to filter and optional table alias.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataScope {

    /**
     * The database column that represents the data owner or scope field.
     * Example: "dept_id" or "tenant_id".
     */
    String column() default "dept_id";

    /**
     * Optional SQL table alias to prefix the column, e.g., "u" to build "u.dept_id".
     */
    String alias() default "";

    /**
     * Whether to ignore the data scope for super admin like roles.
     */
    boolean ignoreForSuperAdmin() default true;
}