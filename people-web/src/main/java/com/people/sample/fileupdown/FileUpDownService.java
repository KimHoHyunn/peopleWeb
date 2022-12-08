package com.people.sample.fileupdown;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.people.common.consts.ErrorCode;
import com.people.common.oldutil.FileUtil;
import com.people.common.oldutil.SystemUtil;
import com.people.common.vo.FileVO;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUpDownService {
	
	String fileRoot;
	
	public void fileUpload(ResponseVO responseVO, MultipartFile multipartFile) {
		
	    if( !multipartFile.isEmpty() ) {
	    	
	        try {
	        	FileVO fileVO = FileUtil.saveFile(multipartFile);
	        	log.info(fileVO.toStringJson());
//	        	Map<String, String> testMap = BeanUtils.describe(fileVO);
	        	
	        	responseVO.setResultData("fileUploadInfo", fileVO);
			} catch (Exception e) {
				log.error(SystemUtil.getExceptionLog(e));
				responseVO = new ResponseVO(ErrorCode.INTERNAL_SERVER_ERROR); 
			}
	    }
		
//		return responseVO;
	}
	
	@Autowired FileInfoDao fileInfoDao;
	
	public ResponseEntity<?> fileDownload(HttpServletRequest request, String fid) throws Exception{
		//데이터베이스에서 파일 관련 데이터를 가져온다.
		FileVO fileVO = fileInfoDao.getFileInfo(fid);
		return FileUtil.fileDownload(request, fileVO);
	}
}
