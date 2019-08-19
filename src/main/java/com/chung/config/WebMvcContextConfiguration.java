package com.chung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.chung.controller" })
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("/res/htmls/mainpage.jsp");
		registry.addViewController("/detail").setViewName("/res/htmls/detail.jsp");
		registry.addViewController("/review").setViewName("/res/htmls/review.jsp");
		registry.addViewController("/reserve").setViewName("/res/htmls/reserve.jsp");
		registry.addViewController("/bookinglogin").setViewName("/res/htmls/bookinglogin.html");
		registry.addViewController("/myreservation").setViewName("/res/htmls/myreservation.jsp");
		registry.addViewController("/reviewWrite").setViewName("/res/htmls/reviewWrite.jsp");
	}

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		return resolver;
	}
}