package com.study.dataentryservice.config;

import com.study.dataentryservice.interceptors.LoggingInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
@ComponentScan (basePackages = "com.study.dataentryservice")
public class ApplicationConfig extends WebMvcConfigurationSupport {

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new LoggingInterceptor());
	}

}
