package com.navent.realestate.examples.connectors;

import feign.Feign;
import feign.Request;
import feign.hystrix.HystrixFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mkfuri on 2/15/17.
 */
@Configuration
public class OAuthConfig {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

   /* @Bean
    public Request.Options options() {
        return new Request.Options(5000, 5000);
    }
*/
    @Bean
    public Feign.Builder feignBuilder() {
        return HystrixFeign.builder();
    }
}

