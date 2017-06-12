package com.fuimaz.athena.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by juchen on 17/5/3.
 */
@Configuration
public class ConfigureTest {

    @Bean(name = "hello2")
    public Hello hello2() {
        return new Hello(2, 2);
    }

    @Bean(name = "hello2")
    public Hello hello1() {
        return new Hello(1, 1);
    }

}
