package com.fuimaz.athena.security;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers {@link DataScopeInterceptor} into Spring context so that
 * MyBatis can pick it up when configured to scan plugins.
 * If you are using mybatis-spring, ensure this bean is injected to SqlSessionFactory.
 */
@Configuration
public class DataScopeAutoConfiguration {

    @Bean
    public Interceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
        
    }
}