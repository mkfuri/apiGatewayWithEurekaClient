package com.navent.realestate.examples;

import com.navent.realestate.examples.feign.HelloWorldServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "helloworld")
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrixDashboard
@RestController
public class ApiGatewayWithEurekaClientApplication {

	private static Logger LOGGER = LoggerFactory.getLogger(ApiGatewayWithEurekaClientApplication.class);

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private HelloWorldServiceClient helloWorldServiceClient;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private StoreIntegration storeIntegration;


	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayWithEurekaClientApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		URI uri = discoveryClient.getInstances("helloworld").get(0).getUri();
		LOGGER.info(String.format("Eureka Plain: Instance %s del servicio 'helloworld'", uri));
		return restTemplate.exchange(uri, HttpMethod.GET, null, String.class).getBody();
	}

	@RequestMapping("/ribbon")
	public String homeRibbon() {
		ServiceInstance serviceInstance = loadBalancer.choose("helloworld");
		URI uriRibbon = serviceInstance.getUri();
		LOGGER.info(String.format("Ribbon: Instance %s del servicio 'helloworld'", uriRibbon));
		return restTemplate.exchange(uriRibbon, HttpMethod.GET, null, String.class).getBody();
	}

	@RequestMapping("/feign")
	public String homeFeign() {
		LOGGER.info(String.format("Feign: Se llama al home vía feign"));
		return helloWorldServiceClient.home();
	}

	@RequestMapping("/hystrixTest")
	public String hystrixTest() {
		LOGGER.info(String.format("hystrixTestgn"));
		return storeIntegration.foo();
	}

	@Component
	class DiscoveryClientExample implements InitializingBean{

		@Autowired
		private DiscoveryClient discoveryClient;

		@Override
		public void afterPropertiesSet() throws Exception {
			discoveryClient.getInstances("helloworld").forEach((ServiceInstance s) -> {
				System.out.println(ToStringBuilder.reflectionToString(s));
			});
		}
	}

	@Component
	public class StoreIntegration {

		private AtomicInteger countUntilFail = new AtomicInteger(0);

		@HystrixCommand(fallbackMethod = "defaultFoo")
		public String foo() {
			countUntilFail.incrementAndGet();
			if (countUntilFail.get() >= 10 && countUntilFail.get() < 20){
				throw new RuntimeException();
			}
			return "Anduvo";
		}

		public String defaultFoo() {
			return "no anda";
		}
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
