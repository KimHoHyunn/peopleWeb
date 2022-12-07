package com.people.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * System 관련 utility
 * @author mh042
 *
 */

@Slf4j
@Component
@Configuration
public class SystemUtil {
	
	public static final String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return ""; 
		}
	}
	
	public static final String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return ""; 
		}
	}
	
	public static final String getExceptionLog(Exception e, String comment) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("[").append(e.getClass().getSimpleName()).append("]");
		sb.append(comment).append(" : ").append(e.getMessage());
		
		//스택정보가져오기
		StackTraceElement[] ste = e.getStackTrace();
		
		for(int i=0; i < ste.length; i++) {
			String className  = ste[i].getClassName();
			String methodName = ste[i].getMethodName();
			int    lineNumber = ste[i].getLineNumber();
			
			if(log.isDebugEnabled() == false && className.startsWith("com.people") == false) {
				continue;
			}
			
			sb.append("\n (").append(className).append(".").append(methodName).append(":").append(lineNumber).append(")");
		}
		
		return sb.toString();
	}
	
	public static final String getExceptionLog(Exception e) {
		return getExceptionLog(e, "");
	}
	
	
	public static String nowTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(System.currentTimeMillis());
	}
	
	
	
	public static final Properties getCommonProperties() throws IOException {
		//PEOPLE-COMMON-PATH은 jar 시작할 때 지정해주는 값이다. shell 참조
		//예 : 
		//#Common Path
		//export NEXT_EDS_COMMON_PATH = ${NEXT_EDS_PATH}/next_eds_common
		//File file = FileUtil.joinPaths(System.getenv("PEOPLE-COMMON-PATH"), FileUtil.PATH.CONFIG, "common.properties");
		File file = FileUtil.joinPaths("D://dev//", FileUtil.PATH.CONFIG, "common.properties");
		
		return getProperties(file);
	}
	
	public static final Properties getConfigProperties() throws IOException {
//		File file = FileUtil.joinPaths(System.getProperty("NEXT_EDS_COMMON_PATH"), FileUtil.PATH.CONFIG, "config.properties");
		File file = FileUtil.joinPaths("", FileUtil.PATH.CONFIG, "config.properties");
		return getProperties(file);
	}
	
	private static final Properties getProperties(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fis);
		fis.close();
		return properties;
	}
	
}
