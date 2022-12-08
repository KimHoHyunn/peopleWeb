package com.people.sample.fileupdown;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.people.common.consts.ErrorCode;
import com.people.common.oldutil.OldSystemUtil;
import com.people.common.util.CommonUtil;
import com.people.common.util.FileUtil;
import com.people.common.util.PropertiesUtil;
import com.people.common.vo.FileVO;
import com.people.common.vo.ResponseVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUpDownService {
	
	@Autowired FileUtil fileUtil;
	@Autowired FileInfoDao fileInfoDao;
	@Autowired PropertiesUtil propertiesUtil;
	@Autowired CommonUtil commonUtil;
	
	public void fileUpload(ResponseVO responseVO, MultipartFile multipartFile) throws IllegalStateException, IOException, InterruptedException {
		
	    if( multipartFile.isEmpty() == false ) {
	    	
        	FileVO fileVO = fileUtil.saveFile(multipartFile);
        	log.info(fileVO.toStringJson());
//	        	Map<String, String> testMap = BeanUtils.describe(fileVO);
        	
        	//DB 저장
        	fileInfoDao.insertFileInfo(fileVO);
    		log.info("download file db path = {}", fileVO.getSave_path());
    		fileVO.setSave_path(propertiesUtil.getFileRootPath()+fileVO.getSave_path());
    		log.info("download file full path = {}", fileVO.getSave_path());
        	
        	responseVO.setResultData("fileUploadInfo", fileVO);
	    }
		
//		return responseVO;
	}
	
	public ResponseEntity<?> fileDownload(HttpServletRequest request, String fid) throws Exception{
		//데이터베이스에서 파일 관련 데이터를 가져온다.
		FileVO fileVO = fileInfoDao.getFileInfo(fid);

		return fileUtil.fileDownload(request, fileVO);
	}
	
	public FileVO getFileInfo(String fid) throws Exception{
		//데이터베이스에서 파일 관련 데이터를 가져온다.
		return fileInfoDao.getFileInfo(fid);
	}
}
