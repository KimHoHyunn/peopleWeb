package com.people.sample.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.consts.ErrorCode;
import com.people.common.consts.FileType;
import com.people.common.exception.CustomException;
import com.people.common.util.CommonUtil;
import com.people.common.util.FileUtil;
import com.people.common.util.PropertiesUtil;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PropertiesController {
	
//    @Value("${common.prop.is}")
//    private String commonProp;
//    
//    @Value("${custom.prop.is}")
//    private String customProp;

    @Autowired CommonUtil commonUtil;
    @Autowired PropertiesUtil propertiesUtil;
    @Autowired FileUtil fileUtil;
	
	
	@GetMapping(path = "/prop/test")
    public ResponseVO  restApiGet(String aa, String bb) {
		
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK);
//		ret.add(commonProp);
		try {
			responseVO.setResultData("ROOT", propertiesUtil.getFileRootPath());
			responseVO.setResultData(FileType.TXT.toString(), propertiesUtil.getFilePath(FileType.TXT));
		} catch (IOException e) {
			log.error(commonUtil.getExceptionLog(e));
			// TODO Auto-generated catch block
			throw new CustomException(ErrorCode.BAD_REQUEST);
		}

		return responseVO;
    }
}
