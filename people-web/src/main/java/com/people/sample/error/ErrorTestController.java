package com.people.sample.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.consts.ErrorCode;
import com.people.common.exception.CustomException;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ErrorTestController {
	
	@Autowired ErrorTestService errorTestService;
	
	@PostMapping("/error/{is}")
    public ResponseEntity<ResponseVO> errorTesting(@PathVariable int is) throws Exception {
		//웬만하면 메소드명 출력은 자제하자
		//오버헤드가 장난 아니기 때문에 실제 개발용으로는 비추.
		log.info(new Object() {}.getClass().getName());
		log.info(new Object() {}.getClass().getEnclosingMethod().getName());
        
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(); 
        map.put("testData1", 11);
        map.put("testData2", 12);
        map.put("testData3", 13);
        map.put("testData4", 14);
        list.add(map);
        
        map = new HashMap<>();
        map.put("testData1", 211);
        map.put("testData2", 212);
        map.put("testData3", 213);
        map.put("testData4", 214);
        list.add(map);

        Map<String, Object> userInfoMmap = new HashMap<>(); 
        userInfoMmap.put("userid", "testUser");
        userInfoMmap.put("name", "Kim Test");
        userInfoMmap.put("userNo", 12323);
        userInfoMmap.put("regData", "20221201");
        
        
        
        
        ResponseVO responseVO = new ResponseVO(ErrorCode.OK);
        responseVO.setResultData("testList",list);
        responseVO.setResultData("userInfo",userInfoMmap);

        map = new HashMap<>();
        if(is == 0) {
        	//errorTestService.raiseException();
            //throw new CustomException(ErrorCode.DISPLAY_NOT_FOUND);
        	throw new CustomException(ErrorCode.NOT_FOUND);
        } else {
        	/**
        HttpHeaders httpHeaders = new HttpHeaders();
        
        httpHeaders 에 뭔가 작업이 필요하면 
                    return new ResponseEntity<>(responseVO, httpHeaders, HttpStatus.OK);
                    에 뭔가 작업이 필요 없으면
                    return ResponseEntity.ok(responseVO);
        	 */
        	return ResponseEntity.ok(responseVO);
        }
    }
}
