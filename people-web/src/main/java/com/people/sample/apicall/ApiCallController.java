package com.people.sample.apicall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.people.common.consts.ErrorCode;
import com.people.common.exception.CustomException;
import com.people.common.oldutil.SystemUtil;
import com.people.common.util.ApiUtil;
import com.people.common.vo.ApiResponseVO;
import com.people.common.vo.ApiVO;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiCallController {

	@GetMapping("/apicall")
    public ResponseEntity<ResponseVO> apicall(@RequestParam String aa) throws Exception {
		//웬만하면 메소드명 출력은 자제하자
		//오버헤드가 장난 아니기 때문에 실제 개발용으로는 비추.
		log.info(new Object() {}.getClass().getName());
		log.info(new Object() {}.getClass().getEnclosingMethod().getName());
		
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK);
		
		try {
			ApiVO apiVO = new ApiVO();
			apiVO.setRequestUrl("http://localhost:8080/rest/postparam");
			apiVO.setAa(aa);
			ApiResponseVO apiResponseVO = ApiUtil.post(apiVO);
			
			if(apiResponseVO.getHttpStatus() == ErrorCode.OK.getHttpStatus()) {
				responseVO.setResultData("result", apiResponseVO.getResponseBody());
			} else {
				responseVO = new ResponseVO(ErrorCode.valueOf(apiResponseVO.getHttpStatus()));
			}
			
		} catch (Exception e) {
			log.error(SystemUtil.getExceptionLog(e));
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
        
		return ResponseEntity.ok(responseVO);
    }
}
