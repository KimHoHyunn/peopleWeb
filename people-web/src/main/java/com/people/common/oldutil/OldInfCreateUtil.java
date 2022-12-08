package com.people.common.oldutil;

import java.io.File;

public interface OldInfCreateUtil {
	/**
	 * 항목별 구분자(개행문자)
	 */
	public static final String SP_CHAR = "\r\n";
	
	/** 
	 * 인코딩
	 */
	public static final String ENCODING_CHARSET = "KSC5601";
	
	public boolean createInfFile(File infFile);
}
