package com.people.card.ctr;

import java.util.Map;
import java.util.Properties;

import com.people.card.ctr.sub.CardSendList;
import com.people.common.oldutil.OldSystemUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardSendCtr {
	public void procJobMain(Map<String, Object > eDocInfo, int threadPoolSize) {
		try {
			Properties props = new Properties();//SystemUtil.getConfigProperties();
			
			CardSendList cardSendList = new CardSendList(eDocInfo, props, threadPoolSize);
			cardSendList.workJobList();
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
		}
	}
}
