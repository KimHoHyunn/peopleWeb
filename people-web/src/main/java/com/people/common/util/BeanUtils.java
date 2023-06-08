package com.people.common.util;

import org.springframework.context.ApplicationContext;

import com.people.common.config.ApplicationContextProvider;

public class BeanUtils {
    public static Object getBean(String bean) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(bean);
    }
}