package com.people.common.oldutil;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.people.common.consts.ErrorCode;
import com.people.common.exception.CustomException;
import com.people.common.util.CommonUtil;
import com.people.common.vo.FileVO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OldFileUtil {
	
	//private FileUtil() {}
	
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

	public static String getFileListByDelimiter(File path, int delimiter) {
		
		StringBuffer sb = new StringBuffer();
		
		List<File> fileInfoArray = OldFileFinder.search(path, OldFileFinder.FILE_FILTER);
		
		Collections.sort(fileInfoArray);
		
		for(File file : fileInfoArray) {
			sb.append(file.getAbsolutePath()).append((char) delimiter);
		}
		
		return sb.toString();
	}
	
	public static boolean exists(File file) {
		return null != file && file.exists();
	}
	
	public static boolean exists(String file) {
		return null != file && exists(new File(file));
	}
	
	public static boolean isFile(File file) {
		//canRead는 별도 추가한것인데.. 이상하거나 중복된 것이면 빼도 됨.
		return exists(file) && file.canRead() && file.isFile();
	}
	
	public static boolean copy(File sourceFile, File targetFile) {
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
					log.error(OldSystemUtil.getExceptionLog(e));
				} finally {
					if( null != out ) try {out.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
					if( null != in ) try {in.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
					if( null != fos ) try {fos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
					if( null != fis ) try {fis.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
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
	

	
	public static boolean create(MultipartFile multipartFile, File targetFile) throws IllegalStateException, IOException {
		mkdirs(targetFile.getParent());

		multipartFile.transferTo(targetFile);
		
		return targetFile.isFile();
		
	}
	

	
	/**
	 * 경로 문자열들을 경로로 조합.
	 * @param first
	 * @param more
	 * @return
	 */
	
	public static File joinPaths(String first, String ... more) {
		File file = new File(first);
		for(String path : more) {
			file = new File(file, path);
		}
		
		return file;
	}
	
	public static boolean toBinaryString(File file, String binary) {
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
			log.error(OldSystemUtil.getExceptionLog(e));
		} finally {
			if( null != baos ) try {baos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
			if( null != fis ) try {fis.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
		}
		
		return result;
			
	}
	
	public static String toBinaryString(File file) {
		
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
			log.error(OldSystemUtil.getExceptionLog(e));
		} finally {
			if( null != baos ) try {baos.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
			if( null != fis ) try {fis.close();} catch(IOException e) {log.error(OldSystemUtil.getExceptionLog(e));}
		}
		
		return binary;
			
	}
	
	public static boolean mkdirs(String path) {
		return OldCommonUtil.isNotEmpty(path) && mkdirs(new File(path));
	}
	
	private static boolean mkdirs(File path) {
		return exists(path) || path.mkdirs();
	}
	
	public static String createFileName () throws InterruptedException {
		UUID u = UUID.randomUUID();
		//Thread.sleep(10);
		return u.toString();
	}
	
	private static String makePath(String baseDir, String fileKind, String fileName) {
		Calendar now = Calendar.getInstance();
		String yyyy = String.valueOf(now.get(Calendar.YEAR));
		String mm = String.valueOf(now.get(Calendar.MONTH)+1);
		String dd = String.valueOf(now.get(Calendar.DATE));
		
		String path = joinPaths(baseDir, fileKind, yyyy, mm, dd, fileName).getPath();
		
		return path;
	}
	
	public static String basePath = "d:/dev/files";
//	@Autowired PropertiesUtil propertiesUtil;
//	public void setBasePath() {
//		try {
//			basePath = propertiesUtil.getFileRootPath();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
