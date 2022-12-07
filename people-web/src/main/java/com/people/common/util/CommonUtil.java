package com.people.common.util;

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
	
	public static boolean isEmpty(Object obj) {
		return (null == obj)
			|| (obj instanceof String && ( (String) obj).isEmpty() )
			|| (obj instanceof ArrayList && ((ArrayList<?>) obj).isEmpty() )
			|| (obj instanceof HashMap && ((HashMap<?, ?>) obj).isEmpty() );
	}
	
	public static boolean isNotEmpty(Object obj) {
		return isEmpty(obj) == false;
	}
	
	public static String objectToString(Object obj) {
		if(null == obj) {
			return "";
		} else {
			return String.valueOf(obj).trim();
		}
	}
	
	public static int objectToInt(Object obj) {
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
	
	public static String replaceString(String targetString, String oldString, String newString) {
		return null == targetString ? "" : targetString.replace(oldString, newString);
	}
	
	public static String getInfCodeType(String val) {
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
	public static String getEccNo(Object obj) {
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
	
	public static int safeObjToInt(Object obj) {
		return safeObjToInt(obj, 0);
	}
	
	public static int safeObjToInt(Object obj, int initValue) {
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
	
	public static String safeObjToStr(Object obj) {
		return safeObjToStr(obj, "");
	}
	public static String safeObjToStr(Object obj, String initValue) {
		if( null == obj) {
			return initValue;
		} else {
			return String.valueOf(obj.toString().trim());
		}
	}
	
	public static String safeReplace(String str, String oldChar, String newChar) {
		if(isEmpty(str)) {
			return "";
		} else {
			return str.replace(oldChar, newChar);
		}
	}
	
	public static String safeReplaceAll(String str, String regex, String replacement) {
		if(isEmpty(str)) {
			return "";
		} else {
			return str.replaceAll(regex, replacement);
		}
	}
	
	
}
