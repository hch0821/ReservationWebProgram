package com.chung.config;

import java.util.List;

import com.chung.argumentresolver.HeaderMapArgumentResolver;
import com.chung.interceptor.LogInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcContextConfiguration implements WebMvcConfigurer {
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("mainpage");
		registry.addViewController("/detail").setViewName("detail");
		registry.addViewController("/review").setViewName("review");
		registry.addViewController("/reserve").setViewName("reserve");
		registry.addViewController("/bookinglogin").setViewName("bookinglogin");
		registry.addViewController("/myreservation").setViewName("myreservation");
		registry.addViewController("/reviewWrite").setViewName("reviewWrite");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new HeaderMapArgumentResolver());
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10485760); // 1024 * 1024 * 10 --> 10MB
		return multipartResolver;
	}
}