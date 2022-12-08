package com.people.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtil {
	
	public boolean isEmpty(Object obj) {
		return (null == obj)
			|| (obj instanceof String && ( (String) obj).isEmpty() )
			|| (obj instanceof ArrayList && ((ArrayList<?>) obj).isEmpty() )
			|| (obj instanceof HashMap && ((HashMap<?, ?>) obj).isEmpty() );
	}
	
	public boolean isNotEmpty(Object obj) {
		return isEmpty(obj) == false;
	}
	
	public String objectToString(Object obj) {
		if(null == obj) {
			return "";
		} else {
			return String.valueOf(obj).trim();
		}
	}
	
	public int objectToInt(Object obj) {
		try {
			if(null == obj) {
				return 0;
			} else {
				return Integer.valueOf(objectToString(obj));
			}
		} catch (NumberFormatException nfe) {
			return 0;
		} finally {

		}
	}
	
	public String replaceString(String targetString, String oldString, String newString) {
		return null == targetString ? "" : targetString.replace(oldString, newString);
	}
	
	public String getInfCodeType(String val) {
		//#-###-####
		String regEx    = "(\\d{1})(\\d{3})(\\d{4})";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(val);
		
		log.debug(val.replaceAll(regEx, "$1-$2-$3"));
		
		if(matcher.matches()) {
			return val.replaceAll(regEx, "$1-$2-$3");
		} else {
			return val;
		}

	}
	
	/**
	 * ECC 추출
	 * '12345678901234567890123456789012345<  LJFLSDJFLDFJLDSJFLFJ ATH4VJ5Z  >' 에서
	 *  12345678901234567890123456789012345만 추출
	 *  
	 * @param obj
	 * @return
	 */
	public String getEccNo(Object obj) {
		String chr = "<";
		String retValue = ""; 
		
		if(!isEmpty(obj)) {
			retValue = objectToString(obj);
			if(retValue.contains(chr)) {
				retValue = retValue.substring(0, retValue.indexOf(chr));
			}
			
			if(35 < retValue.length()) {
				retValue = retValue.substring(0, 35);
			}
			return retValue;
		} else {
			return "";
		}
	}
	
	public int safeObjToInt(Object obj) {
		return safeObjToInt(obj, 0);
	}
	
	public int safeObjToInt(Object obj, int initValue) {
		try {
			if( null == obj) {
				return initValue;
			} else {
				return Integer.valueOf(obj.toString().trim());
			}
		} catch (NumberFormatException e) {
			return initValue;
		}

	}
	
	public String safeObjToStr(Object obj) {
		return safeObjToStr(obj, "");
	}
	public String safeObjToStr(Object obj, String initValue) {
		if( null == obj) {
			return initValue;
		} else {
			return String.valueOf(obj.toString().trim());
		}
	}
	
	public String safeReplace(String str, String oldChar, String newChar) {
		if(isEmpty(str)) {
			return "";
		} else {
			return str.replace(oldChar, newChar);
		}
	}
	
	public String safeReplaceAll(String str, String regex, String replacement) {
		if(isEmpty(str)) {
			return "";
		} else {
			return str.replaceAll(regex, replacement);
		}
	}
	

	
	public String getExceptionLog(Exception e, String comment) {
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
	
	public String getExceptionLog(Exception e) {
		return getExceptionLog(e, "");
	}
	
	
	public String nowTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(System.currentTimeMillis());
	}
	
	
}
