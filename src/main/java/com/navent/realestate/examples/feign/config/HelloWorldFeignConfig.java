package com.navent.realestate.examples.feign.config;

import feign.Feign;
import feign.hystrix.HystrixFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mkfuri on 2/15/17.
 */
@Configuration
public class HelloWorldFeignConfig {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return HystrixFeign.builder();
    }
}
