package com.people.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.people.common.consts.ErrorCode;
import com.people.common.exception.CustomException;
import com.people.common.vo.FileVO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {
	
	//private FileUtil() {}

	@Autowired FileFinder fileFinder;
	@Autowired CommonUtil commonUtil;
	@Autowired PropertiesUtil propertiesUtil;
	
	public static final class PATH {
		public static final String CONFIG 	= "config";
		public static final String TSA 		= "tsa";
		public static final String IDX 		= "idx";
		public static final String IMAGE 	= "img";
		public static final String CARD		= "card";
		public static final String FILE		= "etc";
	}
	
	public static final class EXT {
		public static final String TSA 		= ".tsa";
		public static final String IDX 		= ".idx";
		public static final String IMAGE 	= ".tif";
		public static final String INF 		= ".inf";
	}

	public String getFileListByDelimiter(File path, int delimiter) {
		
		StringBuffer sb = new StringBuffer();
		
		List<File> fileInfoArray = fileFinder.search(path, fileFinder.FILE_FILTER);
		
		Collections.sort(fileInfoArray);
		
		for(File file : fileInfoArray) {
			sb.append(file.getAbsolutePath()).append((char) delimiter);
		}
		
		return sb.toString();
	}
	
	public boolean exists(File file) {
		return null != file && file.exists();
	}
	
	public boolean exists(String file) {
		return null != file && exists(new File(file));
	}
	
	public boolean isFile(File file) {
		//canRead는 별도 추가한것인데.. 이상하거나 중복된 것이면 빼도 됨.
		return exists(file) && file.canRead() && file.isFile();
	}
	
	public boolean copy(File sourceFile, File targetFile) {
		boolean result = false;

		FileInputStream fis  = null;
		FileOutputStream fos = null;
		
		FileChannel in  = null;
		FileChannel out = null;
		
		if(isFile(sourceFile)) {
			if(mkdirs(targetFile.getParent())) {
				try {
					fis = new FileInputStream(sourceFile);
					fos = new FileOutputStream(targetFile);
					
					//채널생성
					in = fis.getChannel();
					out = fos.getChannel();
					
					//생성된 채널을 통해 스트림 전송
					in.transferTo(0, in.size(), out);
					result = true;
					
				} catch (IOException e) {
					log.error(commonUtil.getExceptionLog(e));
				} finally {
					if( null != out ) try {out.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
					if( null != in ) try {in.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
					if( null != fos ) try {fos.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
					if( null != fis ) try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
				}
			} else {
				log.error("Create Directory Error~");
			}
		} else {
			log.error("Source file is not found");
			//new CustomException(ErrorCode.USER_NOT_FOUND);
		}
		
		return result;
		
	}
	

	
	public boolean create(MultipartFile multipartFile, File targetFile) throws IllegalStateException, IOException {
		mkdirs(targetFile.getParent());

		multipartFile.transferTo(targetFile);
		
		return targetFile.isFile();
		/**
		File originalFile = new File( multipartFile.getOriginalFilename());
		originalFile.createNewFile(); 

		FileInputStream fis  = null;
		FileOutputStream fos = null;
		
		FileChannel in  = null;
		FileChannel out = null;
		
		if(mkdirs(targetFile.getParent())) {
			try {
				fis = new FileInputStream(originalFile);
				fos = new FileOutputStream(targetFile);
				
				//채널생성
				in = fis.getChannel();
				out = fos.getChannel();
				
				//생성된 채널을 통해 스트림 전송
				in.transferTo(0, in.size(), out);
				result = true;
				
			} catch (IOException e) {
				log.error(commonUtil.getExceptionLog(e));
			} finally {
				if( null != out ) try {out.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
				if( null != in  ) try {in.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
				if( null != fos ) try {fos.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
				if( null != fis ) try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
			}
		} else {
			log.error("Create Directory Error~");
		}
		*/
		
	}
	
	/**
	 * 경로 문자열들을 경로로 조합.
	 * @param first
	 * @param more
	 * @return
	 */
	
	public File joinPaths(String first, String ... more) {
		File file = new File(first);
		for(String path : more) {
			file = new File(file, path);
		}
		
		return file;
	}
	
	public boolean toBinaryString(File file, String binary) {
		boolean result = false;
		
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		
		try {
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream();
			
			int len = 0;
			byte[] buf = new byte[1024];
			while( (len = fis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			
			binary = new String(Base64.encodeBase64(baos.toByteArray()));
			
			result = true;
			
		} catch(IOException e) {
			log.error(commonUtil.getExceptionLog(e));
		} finally {
			if( null != baos ) try {baos.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
			if( null != fis ) try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
		}
		
		return result;
			
	}
	
	public String toBinaryString(File file) {
		
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		
		String binary = "";
		
		try {
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream();
			
			int len = 0;
			byte[] buf = new byte[1024];
			while( (len = fis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			
			binary = new String(baos.toByteArray());
			
		} catch(IOException e) {
			log.error(commonUtil.getExceptionLog(e));
		} finally {
			if( null != baos ) try {baos.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
			if( null != fis ) try {fis.close();} catch(IOException e) {log.error(commonUtil.getExceptionLog(e));}
		}
		
		return binary;
			
	}
	
	public boolean mkdirs(String path) {
		return commonUtil.isNotEmpty(path) && mkdirs(new File(path));
	}
	
	private boolean mkdirs(File path) {
		return exists(path) || path.mkdirs();
	}
	
	public String createFileName () throws InterruptedException {
		UUID u = UUID.randomUUID();
		//Thread.sleep(10);
		return u.toString();
	}
	
	private String makePath(String baseDir, String fileKind, String fileName) {
		Calendar now = Calendar.getInstance();
		String yyyy = String.valueOf(now.get(Calendar.YEAR));
		String mm = String.valueOf(now.get(Calendar.MONTH)+1);
		String dd = String.valueOf(now.get(Calendar.DATE));
		
		String path = joinPaths(baseDir, fileKind, yyyy, mm, dd, fileName).getPath();
		
		return commonUtil.safeReplace(path, "\\", "/");
	}
	
	public FileVO saveFile(MultipartFile originalFile) throws IllegalStateException, IOException, InterruptedException {
		
		log.debug("originalFile = {}", originalFile.getSize());
		
		FileVO fileVO = new FileVO();
		
		fileVO.setSize(originalFile.getSize());
		
    	String originalFileName = originalFile.getOriginalFilename();
    	fileVO.setOrigin_name(originalFileName);
        log.debug("file org name = {}", originalFileName);
        
        String originalFileType = originalFile.getContentType();
        fileVO.setContent_type(originalFileType);
        log.debug("file content type = {}", originalFile.getContentType());
        
        String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        fileVO.setExtension(fileExt);
        log.debug("file extension = {}", fileExt);
        
    	String saveFileName = createFileName();
    	fileVO.setSave_name(saveFileName+"."+fileExt);
    	log.info("--- saveFileName = "+ saveFileName);
    	
    	String basePath = propertiesUtil.getFileRootPath();
    	log.info("--- basePath     = "+ basePath);
       	String subPath = "/" + makePath(PATH.FILE.toString(),"", "");
    	log.info("--- subPath     = "+ subPath);
       	String saveFilePath =basePath+subPath+"/"+saveFileName+"."+fileExt;
       	log.info("--- saveFilePath = "+ saveFilePath);
       	
       	File targetFile = new File(saveFilePath);
       	fileVO.setSave_path(subPath);
       	log.info("--- getParent = "+ fileVO.getSave_path());
       	create(originalFile, targetFile);
       	
       	return fileVO;
	}
	
	public ResponseEntity<?> fileDownload(HttpServletRequest request, FileVO fileVO) throws Exception{
		if(commonUtil.isNotEmpty(fileVO)) {
			HttpHeaders headers = new HttpHeaders();
	
	//			contentType = Files.probeContentType(path);
	//			headers.add(HttpHeaders.CONTENT_TYPE, contentType);
	//			headers.setContentDisposition(ContentDisposition.builder("attachment")
	//					                                        .filename(fileVO.getOriginalFileName(), StandardCharsets.UTF_8)
	//					                                        .build());
			
			String userAgent = request.getHeader("User-Agent");
			boolean isBrowser = false;
	
			String encordedFilename = fileVO.getOrigin_name();
			String attachment       = "";
			if(userAgent.contains("Edge")) {
				isBrowser = true;
				encordedFilename = URLEncoder.encode(encordedFilename,"UTF-8").replace("\\+", "%20");
				attachment = "attachment;filename=\""+encordedFilename;
			} else if(userAgent.contains("MSIE") || userAgent.contains("Trident")  ) {
				isBrowser = true;
				encordedFilename = URLEncoder.encode(encordedFilename,"UTF-8").replace("\\+", "%20");
				attachment = "attachment;filename=\""+encordedFilename;
			} else if(userAgent.contains("Chrome")  ) {
				isBrowser = true;
				encordedFilename = new String(encordedFilename.getBytes("UTF-8"),"ISO-8859-1");
				attachment = "attachment;filename="+encordedFilename;		
			} else if(userAgent.contains("Firefox")  ) {
				isBrowser = true;
				encordedFilename = new String(encordedFilename.getBytes("UTF-8"),"ISO-8859-1");
				attachment = "attachment;filename="+encordedFilename;	
			} else if(userAgent.contains("Postman")  ) {
				isBrowser = true;
				encordedFilename = new String(encordedFilename.getBytes("UTF-8"),"ISO-8859-1");
				attachment = "attachment;filename="+fileVO.getSave_name();
	//		} else {
	//			throw new CustomException(ErrorCode.BROWSER_NOT_FOUND);
			}
			
			log.info("download file db path = {}", fileVO.getSave_path());
			fileVO.setSave_path(propertiesUtil.getFileRootPath()+fileVO.getSave_path());
			log.info("download file full path = {}", fileVO.getSave_path());
			
			if(isBrowser) {
				headers.add("Content-Disposition", attachment);
				headers.add("Content-Type", "application/octet-stream");                
				headers.add("Content-Transfer-Encoding", "binary;");
				headers.add("Pragma", "no-cache;");
				headers.add("Expires", "-1;");
				Path path = Paths.get(fileVO.getSave_path() + "/" + fileVO.getSave_name());
				Resource resource = new InputStreamResource(Files.newInputStream(path));
				return new ResponseEntity<>(resource, headers, HttpStatus.OK);
			} else {
				throw new CustomException(ErrorCode.BROWSER_NOT_FOUND);
			}
		} else {
			throw new CustomException(ErrorCode.FILEDATA_NOT_FOUND);
		}
	}
}
