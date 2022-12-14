package com.people.common.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.people.common.consts.FileType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Properties 관련 utility
 * @author mh042
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class PropertiesUtil {
	@Autowired Environment env;
	
	private String getProperties (String name) throws IOException {
		return env.getRequiredProperty(name);
	}
	
	public String getFileRootPath () throws IOException {
		return getProperties("FILE-ROOT-DIRECTORY");
	}
	public String getFilePath (FileType fileType) throws IOException {
		return getProperties("FILE-"+fileType.toString()+"-DIRECTORY");
	}
	
}
