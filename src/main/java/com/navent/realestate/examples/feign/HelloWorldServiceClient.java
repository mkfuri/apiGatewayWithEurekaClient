package com.navent.realestate.examples.feign;

import com.navent.realestate.examples.feign.config.HelloWorldFeignConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by mkfuri on 2/15/17.
 */
@FeignClient(value = "helloworld", configuration = HelloWorldFeignConfig.class, fallback = HelloWorldFallBack.class)
public interface HelloWorldServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    String home();

}
