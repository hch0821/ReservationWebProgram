package com.chung.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chung.argumentresolver.HeaderMapArgumentResolver;
import com.chung.interceptor.LogInterceptor;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.chung.controller" })
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	Environment env;
	
	public static String ROOTPATH;

	public static String COMMENT_IMAGE_PATH;
	
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
		
		ROOTPATH = env.getProperty("rootpath");
		COMMENT_IMAGE_PATH = env.getProperty("commentimagepath");
	}

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		return resolver;
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