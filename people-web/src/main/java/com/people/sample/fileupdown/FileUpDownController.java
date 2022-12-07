package com.people.sample.fileupdown;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.people.common.consts.ErrorCode;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FileUpDownController {
	@Autowired FileUpDownService fileUpDownService; 
	
	@PostMapping("/file/up")
	public ResponseEntity<ResponseVO> uploadFile(MultipartFile multipartFile) {
		ResponseVO responseVO = new ResponseVO(ErrorCode.OK); 
		
		fileUpDownService.fileUpload(responseVO, multipartFile);
		log.info("responseVO = {}", responseVO.toStringJson());
	    return ResponseEntity.ok(responseVO);
	}
	
	@GetMapping("/file/down/{fid}")
	public ResponseEntity<?> downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String fid) {
		try {
			return fileUpDownService.fileDownload(request, fid);
		} catch (Exception e) {
			return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
		}
	}
}