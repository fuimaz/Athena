package com.fuimaz.athena.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Holds the current data scope context for the calling thread.
 * Typical usage: set current tenant or department ids before invoking mapper.
 */
public final class DataScopeContext {

    private static final ThreadLocal<Context> CONTEXT = ThreadLocal.withInitial(Context::new);

    private DataScopeContext() {}

    public static void clear() {
        CONTEXT.remove();
    }

    public static void setSuperAdmin(boolean superAdmin) {
        CONTEXT.get().superAdmin = superAdmin;
    }

    public static boolean isSuperAdmin() {
        return CONTEXT.get().superAdmin;
    }

    public static void setScopeIds(Set<Long> ids) {
        CONTEXT.get().scopeIds = (ids == null ? new HashSet<>() : new HashSet<>(ids));
    }

    public static Set<Long> getScopeIds() {
        Set<Long> ids = CONTEXT.get().scopeIds;
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(ids);
    }

    public static void addScopeId(Long id) {
        if (Objects.nonNull(id)) {
            Context ctx = CONTEXT.get();
            if (ctx.scopeIds == null) {
                ctx.scopeIds = new HashSet<>();
            }
            ctx.scopeIds.add(id);
        }
    }

    private static final class Context {
        private boolean superAdmin;
        private Set<Long> scopeIds;
    }
}