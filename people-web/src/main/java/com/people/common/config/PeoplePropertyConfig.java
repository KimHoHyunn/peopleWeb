package com.people.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
	@PropertySource("classpath:config/common-${spring.profiles.active}.properties")
	, @PropertySource("classpath:config/people-${spring.profiles.active}.properties")
})
public class PeoplePropertyConfig {

}
