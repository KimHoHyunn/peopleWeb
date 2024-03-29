package com.people.sample.restapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.config.DatabaseProperties;
import com.people.common.consts.ErrorCode;
import com.people.common.util.CommonUtil;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RestApiController {
	
	@Autowired RestApiService restApiService;
	@Autowired CommonUtil commonUtil; 

	@GetMapping(path = "/rest/get")
    public ResponseEntity<?>  restApiGet(String aa, String bb) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(commonUtil.isNotEmpty(aa)) params.put("aa",aa);
		if(commonUtil.isNotEmpty(bb)) params.put("bb",bb);
		
		List<Map<String, Object>> ret = restApiService.getData(params);

		return ResponseEntity.ok().body(ret);
    }
	
	@PostMapping(path = "/rest/pathvar/{aa}/{bb}")
    public ResponseEntity<?>  restApiPathVariable(@PathVariable("aa") String param1, @PathVariable int bb) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("aa",param1);
		params.put("bb",bb);
		
		List<Map<String, Object>> ret = restApiService.getData(params);

		return ResponseEntity.ok().body(ret);
    }
	
	@PostMapping(path = "/rest/jsonparam", produces = "application/json; charset=utf-8")
    public ResponseEntity<?>  restApiJsonParam(@RequestBody Map<String,Object> param) {
		
		List<Map<String, Object>> ret = restApiService.getData(param);

		return ResponseEntity.ok().body(ret);
    }
	
	@PostMapping(path = "/rest/postparam")
    public ResponseEntity<?>  restApiPostParam(String aa, String bb) {
		//웬만하면 메소드명 출력은 자제하자
		//오버헤드가 장난 아니기 때문에 실제 개발용으로는 비추.
		log.info(new Object() {}.getClass().getName());
		log.info(new Object() {}.getClass().getEnclosingMethod().getName());
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(commonUtil.isNotEmpty(aa)) params.put("aa",aa);
		if(commonUtil.isNotEmpty(bb)) params.put("bb",bb);
		List<Map<String, Object>> ret = restApiService.getData(params);
		
		return ResponseEntity.ok().body(ret);
    }
	
	@PostMapping(path = "/rest/voparam")
    public ResponseVO  restApiVoParam(RestApiVO restApiVO) {
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK);

		Map<String, Object> params = new HashMap<String, Object>();
		if(commonUtil.isNotEmpty(restApiVO.getAa())) params.put("aa",restApiVO.getAa());
		if(commonUtil.isNotEmpty(restApiVO.getBb())) params.put("bb",restApiVO.getBb());

		responseVO.setResultData(restApiService.getData(params));
		return responseVO;
    }
	
	@PostMapping(path = "/rest/property")
    public ResponseVO  getDBProperties() {
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK);
//		DatabaseProperties databaseProperties = new DatabaseProperties();
//		responseVO.setResultData(databaseProperties.getDriverClassName());
//		responseVO.setResultData(databaseProperties.getJndiName());
//		responseVO.setResultData(databaseProperties.getUrl());
//		responseVO.setResultData(databaseProperties.getUsername());
//		responseVO.setResultData(databaseProperties.getPassword());
		return responseVO;
    }
	
}
