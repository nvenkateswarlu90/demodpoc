package com.a4tech.shipping.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
//@EnableWebMvc
@Configuration
public class WebConfig /*extends WebMvcConfigurerAdapter*/{
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource resource = new ResourceBundleMessageSource();
		resource.setBasename("errorMessage");
		return resource;
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
