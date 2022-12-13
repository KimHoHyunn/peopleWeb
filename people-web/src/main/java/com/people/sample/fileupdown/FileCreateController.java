package com.people.sample.fileupdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.consts.ErrorCode;
import com.people.common.consts.FileType;
import com.people.common.util.FileUtil;
import com.people.common.vo.FileVO;
import com.people.common.vo.ResponseVO;

@RestController
public class FileCreateController {

	@Autowired FileUtil fileUtil; 
	@Autowired FileUpDownService fileUpDownService;
	
	@PostMapping("/file/c/json")
	public ResponseVO uploadFile(@RequestBody String jsonStr) throws Exception {
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK); 
		
		try {
			FileVO fileVO = fileUtil.create(jsonStr, FileType.JSON, "testJsonFile");
			fileUpDownService.saveFileInfo(fileVO);
			responseVO.setResultData("jsonFileInfo", fileVO);
			
			fileVO = fileUtil.create(jsonStr, FileType.TXT, "testTextFile");
			fileUpDownService.saveFileInfo(fileVO);
			responseVO.setResultData("txtFileInfo", fileVO);
		} catch(Exception e) {
			
		}
		
	    return responseVO;
	}
}
