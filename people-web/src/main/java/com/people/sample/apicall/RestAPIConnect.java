package com.people.sample.apicall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.common.util.CommonUtil;
import com.people.common.util.FileUtil;
import com.people.common.util.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAPIConnect {
	
	@Autowired PropertiesUtil propertiesUtil;
	@Autowired CommonUtil commonUtil;
	@Autowired FileUtil fileUtil;
	
    private String getBase64String(MultipartFile multipartFile) throws Exception {
        byte[] bytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    @SuppressWarnings("unused")
	private byte [] getFileToByte(File file)  {
    	InputStream fis = null;
    	ByteArrayOutputStream baos = null;
    	byte [] fileData = null;
    	try {
			// [3]. FileInputStream 을 사용해 파일 읽음
    		fis = new FileInputStream(file);
			
			// [4]. File to Byte 변환 실시
    		baos = new ByteArrayOutputStream();
			int len = fis.read();
			while (len != -1) {
				baos.write(len);
			    len = fis.read();
			}
			
			fileData = baos.toByteArray();
			
    	}catch (Exception e) {
			log.error(commonUtil.getExceptionLog(e));
		}finally {
			if(commonUtil.isNotEmpty(fis)) { try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}}
			if(commonUtil.isNotEmpty(baos)) { try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}}
		}

        return fileData;
    }
	
	public RestVO apiCall(String fileName, MultipartFile file) throws Exception {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(5000);  // 읽기시간초과, ms
//        factory.setConnectTimeout(3000); // 연결시간초과, ms
//        HttpClient httpClient = HttpClientBuilder.create()
//            .setMaxConnTotal(100) // connection pool 적용
//            .setMaxConnPerRoute(5) // connection pool 적용
//            .build();
//        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
//        RestTemplate restTemplate = new RestTemplate(factory);
//
//        String url = "http://testapi.com/search?text=1234"; // 예제니까 애초에 때려박음..
//
//        Object obj = restTemplate.getForObject("요청할 URI 주소", "응답내용과 자동으로 매핑시킬 java object");
//
//        System.out.println(obj);
		
		
//		  HttpHeaders requestHeaders = new HttpHeaders();
//		  requestHeaders.set("MyHeader", "MyValue");
//		  HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
//		  ResponseEntity<String> response =
//		      template.exchange(baseUrl + "/{method}", HttpMethod.GET, requestEntity, String.class, "get");
//		  assertEquals("Invalid content", helloWorld, response.getBody());
		
        RestTemplate restTemplate = new RestTemplate();
        
        String url = "";//propertiesUtil.getReatAptUrl();
        
        String imageFileString = getBase64String(file);

        // Header set
        HttpHeaders headers = new HttpHeaders();
       
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getName());
        headers.setContentLength(imageFileString.length());

        // Body set
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("filename", fileName);
        parameters.add("image", imageFileString);
        
        File pFile = new File(file.getOriginalFilename());
        parameters.add("imageByte", fileUtil.toBinaryString(pFile));

        // Message
        HttpEntity<?> requestMessage = new HttpEntity<>(parameters, headers);

        // Request
        HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);

        // Response 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        RestVO vo = objectMapper.readValue(response.getBody(), RestVO.class);

        return vo;
		
	}
}
