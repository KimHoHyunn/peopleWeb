package com.people.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseProperties {

//	@Value("${spring.datasource.jdbc-url}") 
	String url;
	
//	@Value("${spring.datasource.username}") 
	String username;
	
//	@Value("${spring.datasource.password}") 
	String password;
	
//	@Value("${spring.datasource.driver-class-name}") 
	String driverClassName;
	
//	@Value("${spring.datasource.jndi-name}") 
	String jndiName;

	// getters and setters

}