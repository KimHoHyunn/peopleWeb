package com.people.img.ctr;

import java.util.List;
import java.util.Properties;

import com.people.common.util.SystemUtil;
import com.people.img.ctr.sub.ImgCreateList;

public class ImgCreateCtr {
	public void procJobMain(List<String> eDocIdxNoList, int threadPoolSize) {
		try {
			Properties props = SystemUtil.getConfigProperties();
			
			//이미지변환(생성) 처리 work list 생성
			ImgCreateList imgCreateList = new ImgCreateList(eDocIdxNoList, props, threadPoolSize);
			imgCreateList.workJobList();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
