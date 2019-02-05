package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bh.api.proxy.gateway.model.ObjectCache;
import com.bh.api.proxy.gateway.model.ResponseCache;

@SpringBootApplication
public class App {
	public static void main(String... args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public ObjectCache getObjectCache() {
		return ObjectCache.initialize();
	}
	
	@Bean
	public ResponseCache getResponseCache() {
		return ResponseCache.initialize();
	}
	
	 @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurerAdapter() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/").allowedOrigins("http://localhost:3000");
	                registry.addMapping("/**/*").allowedOrigins("http://localhost:3000");
	            }
	        };
	    }
}
