package com.chung.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.chung.dao", "com.chung.service"})
@Import({ DBConfig.class})
public class ApplicationConfig {
	
}
