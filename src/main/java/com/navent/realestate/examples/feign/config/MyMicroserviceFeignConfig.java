package com.navent.realestate.examples.feign.config;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mkfuri on 2/15/17.
 */
@Configuration
public class MyMicroserviceFeignConfig {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
