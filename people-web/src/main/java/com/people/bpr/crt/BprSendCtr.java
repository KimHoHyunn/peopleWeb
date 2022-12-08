package com.people.bpr.crt;

import java.util.Map;
import java.util.Properties;

import com.people.bpr.crt.sub.BprSendList;
import com.people.common.oldutil.OldSystemUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BprSendCtr {
	public void procJobMain(Map<String, Object> eDocInfo, int threadPoolSize, String bprSendG) {
		try {
			Properties props = new Properties();// SystemUtil.getConfigProperties();
			BprSendList bsList = new BprSendList(eDocInfo, props, threadPoolSize, bprSendG);
			bsList.workJobList();
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		}
	}
}
