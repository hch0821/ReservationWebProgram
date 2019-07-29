package com.chung.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ComponentScan(basePackages = { "com.chung.dao",  "com.chung.service"})
@Import({ DBConfig.class })
public class ApplicationConfig {

}
