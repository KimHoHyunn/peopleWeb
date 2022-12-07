package com.people.common.env;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration						
@RequiredArgsConstructor
@PropertySource("classpath:config/common.properties") 	
public class PropertiesConfig {
	
    private final Environment environment;		// 빈 주입을 받습니다.

    public String getCommonProperty(String key){
        return environment.getProperty(key);
    }
	
}