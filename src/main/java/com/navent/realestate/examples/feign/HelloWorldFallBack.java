package com.navent.realestate.examples.feign;

import org.springframework.stereotype.Component;

/**
 * Created by mkfuri on 7/8/17.
 */
@Component
public class HelloWorldFallBack implements HelloWorldServiceClient{
    @Override
    public String home() {
        return "Chauuuuuu";
    }
}
