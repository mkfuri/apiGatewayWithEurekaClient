package com.navent.realestate.examples.connectors;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@FeignClient(name = "api-core")
public interface OAuth {
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    OAuth2AccessToken getToken(@RequestHeader("Authorization") String authentication,
                               @RequestParam(name = "grant_type") String grantType,
                               @RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "scope") String scope);
}
