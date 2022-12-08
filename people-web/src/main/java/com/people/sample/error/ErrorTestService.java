package com.people.sample.error;

import org.springframework.stereotype.Service;

import com.people.common.oldutil.OldSystemUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrorTestService {
	private static String paramStr;
	public void raiseException() {
		
		try {
			if(paramStr == null) {
				//throw new CustomException(ErrorCode.INVALID_PARAMETER);
				throw new NullPointerException();
			}
		} catch (Exception e) {
			// TODO: handle exception
//			log.info(SystemUtil.getExceptionLog(e, "Exception Test : paramStr "));
			log.info(OldSystemUtil.getExceptionLog(e));
			throw e;
		}
	}
}
