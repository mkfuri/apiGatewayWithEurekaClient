package com.navent.realestate.examples;

import com.navent.realestate.examples.feign.MyMicroServiceClient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "myMicroService")
@EnableFeignClients
@RestController
public class ApiGatewayWithEurekaClientApplication {

	private static Logger LOGGER = LoggerFactory.getLogger(ApiGatewayWithEurekaClientApplication.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private MyMicroServiceClient myMicroserviceClient;

	@Autowired
	private RestTemplate restTemplate;


	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayWithEurekaClientApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		URI uri = discoveryClient.getInstances("myMicroService").get(0).getUri();
		LOGGER.info(String.format("Eureka Plain: Instance %s del servicio 'myMicroService'", uri));
		return restTemplate.exchange(uri, HttpMethod.GET, null, String.class).getBody();
	}

	@RequestMapping("/ribbon")
	public String homeRibbon() {
		ServiceInstance serviceInstance = loadBalancer.choose("myMicroService");
		URI uriRibbon = serviceInstance.getUri();
		LOGGER.info(String.format("Ribbon: Instance %s del servicio 'myMicroService'", uriRibbon));
		return restTemplate.exchange(uriRibbon, HttpMethod.GET, null, String.class).getBody();
	}

	@RequestMapping("/feign")
	public String homeFeign() {
		LOGGER.info(String.format("Feign: Se llama al home vía feign"));
		return myMicroserviceClient.home();
	}

	@Component
	class DiscoveryClientExample implements InitializingBean{

		@Autowired
		private DiscoveryClient discoveryClient;

		@Override
		public void afterPropertiesSet() throws Exception {
			discoveryClient.getInstances("my-service").forEach((ServiceInstance s) -> {
				System.out.println(ToStringBuilder.reflectionToString(s));
			});
		}

		/*@Override
		public void run(String... strings) throws Exception {
			discoveryClient.getInstances("photo-service").forEach((ServiceInstance s) -> {
				System.out.println(ToStringBuilder.reflectionToString(s));
			});
			discoveryClient.getInstances("bookmark-service").forEach((ServiceInstance s) -> {
				System.out.println(ToStringBuilder.reflectionToString(s));
			});
		}*/
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
