package com.people.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Properties 관련 utility
 * @author mh042
 *
 */
@Slf4j
@Configuration
public class PropertiesUtil {
	
	public String getFileBasePath () throws IOException {

		Properties prop = new Properties();
		
		try {
		    prop.load(new FileInputStream("D:\\dev\\workspace\\people-web\\bin\\main\\config\\common.properties"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return prop.getProperty("file.root.dir");
	}
}
