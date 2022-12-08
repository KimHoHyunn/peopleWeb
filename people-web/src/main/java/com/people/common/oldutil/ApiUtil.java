package com.people.common.oldutil;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.common.vo.ApiResponseVO;
import com.people.common.vo.ApiVO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//유틸 클래스이니 상속이나 인스턴스 생성을 막기위해 private scope의 기본생성자를 만들어둡니다.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUtil {
	public static ApiResponseVO post(ApiVO apiVO) throws JsonMappingException, JsonProcessingException {
		// 헤더 만들기
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		
		// create an instance of RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// HTTP Body로 들어갈 것들 만들기
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		log.info("apiVO.toStringJson() = {}", apiVO.toStringJson());
		Map<String, Object> map = objectMapper.convertValue(apiVO, new TypeReference<Map<String, Object>>() {});
		params.setAll(map);
		log.info("params = {}", params);
		
//		params.add("grant_type", "authorization_code");
		
		// Set http entity -> Body 데이터와 헤더 묶기
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		// url, request(헤더랑 파라미터 정보), response 타입
		// 여기는 바디 데이터와 헤더가 필요해서 넣었지만 
		// 단순히 데이터만 넣어줘도 되는 경우 현재 request 자리에 해당 객체를 바로 넣어주면 된다.
		ResponseEntity<String> response = restTemplate.postForEntity(apiVO.getRequestUrl(), request, String.class);

		// 전달받은 데이터를 gson 라이브러리를 사용해서 바로 매핑시킬 수 있음
		ApiResponseVO apiResponseVO = new ApiResponseVO();
		
		if(CommonUtil.isEmpty(apiVO.getAa())) {
			apiResponseVO.setHttpStatus(HttpStatus.BAD_REQUEST.value());
			apiResponseVO.setResponseBody(null);
		} else {		
			apiResponseVO.setHttpStatus(response.getStatusCodeValue());
			apiResponseVO.setResponseBody(response.getBody());
		}
//		if (response.getStatusCode() == HttpStatus.OK) {
//		    return gson.fromJson(response.getBody(), RetKakaoAuth.class);
//		}

		// 이런 식으로 바로 꺼낼 수도 있음
		//Member member = restTemplate.getForEntity(builder.toUri(),Member.class).getBody();
		return apiResponseVO;
	}
}
