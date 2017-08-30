package com.navent.realestate.examples;

import com.navent.realestate.examples.connectors.OAuth;
import com.navent.realestate.examples.connectors.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@SpringBootApplication
@RibbonClient("api-core")
@EnableFeignClients
@RestController
public class ApiGatewayWithEurekaClientApplication {

	private static Logger LOGGER = LoggerFactory.getLogger(ApiGatewayWithEurekaClientApplication.class);

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private OAuth oAuth;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayWithEurekaClientApplication.class, args);
	}

	@RequestMapping("/oauth")
	public OAuth2AccessToken homeFeign() throws UnsupportedEncodingException {

		LOGGER.info("oauth");
		String clientId = "mobile_ZPAR";
		String clientCredentials = "mobile1234";
		String username = "producto+zpar-inmobiliaria@navent.com";
		String password = "Producto123";

		String authenticationDecode = clientId + ":"+ clientCredentials;

		String basicAuthentication = "Basic " + Base64.getEncoder().encodeToString(authenticationDecode.getBytes("UTF-8"));
		OAuth2AccessToken a = oAuth.getToken(basicAuthentication, "password", username, password, null);
		return a;
	}
}
