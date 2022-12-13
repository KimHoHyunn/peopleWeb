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
	
	public String getCommonPropIs () throws IOException {
		return getProperties("common.prop.is");
	}
	
	public String getFileRootPath () throws IOException {
		return getProperties("FILE-ROOT-DIRECTORY");
	}
	public String getFilePath (FileType filePath) throws IOException {
		return getProperties("FILE-"+filePath.toString()+"-DIRECTORY");
	}
	
	public String getCustomPropIs () throws IOException {
		return getProperties("custom.prop.is");
	}
	
	public String getReatAptUrl() throws IOException {
		return getProperties("REAT_APT_URL");
	}
	
	
}
