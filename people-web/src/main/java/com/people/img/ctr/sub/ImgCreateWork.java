package com.people.img.ctr.sub;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.people.common.dao.EdsDao;
import com.people.common.oldutil.OldCommonUtil;
import com.people.common.oldutil.OldFileUtil;
import com.people.common.oldutil.OldSystemUtil;
import com.people.common.vo.DirFileVO;
import com.people.img.dao.DaoSelect;
import com.people.img.dao.DaoUpdate;
import com.people.img.util.ImgUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImgCreateWork implements Callable<DirFileVO> {
	
	private String prefixLogStr = "";
	private String middleLogStr = "";
	
	private int comprate = 0;
	private String eDocIdxNo = "";

	public ImgCreateWork(String eDocIdxNo, int comprate) {
		this.eDocIdxNo = eDocIdxNo;
		this.comprate = comprate;
		
		this.prefixLogStr = EdsDao.getEDocProcStep().toString() + " " + eDocIdxNo;
		this.middleLogStr = this.eDocIdxNo;
	}
	
	@Override
	public DirFileVO call() throws Exception {
		log.info(prefixLogStr+" start");
		List<Map<String, Object>> list = DaoSelect.selectFileTarget(eDocIdxNo);
		
		//이미지인덱스번호가 있는 경우 대표서식키(BPR전송)로 지정한다.
		//select에서 order by로 첫번째가 대표서식키로 나오도록 Query 개발됨.
		String repBprKey = OldCommonUtil.objectToString(list.get(0).get("IMG_IDX_NO"));
		if(OldCommonUtil.isNotEmpty(list)) {
			for(Map<String, Object> map : list) {
				DirFileVO dirFileVO = imgCrt(map,repBprKey);
			}
		} else {
			log.info("Data Not Found");
		}
		
		return null;
	}
	
	private DirFileVO imgCrt(Map<String, Object> map, String repBprKey) {
		DirFileVO dirFileVO = new DirFileVO();
		
		int imgSer = OldCommonUtil.safeObjToInt(map.get("IMG_SER"));
		int eDocProcSeqV = OldCommonUtil.safeObjToInt(map.get("EDOC_PROC_SEQ_V"));
		String docFormC = OldCommonUtil.safeObjToStr(map.get("DOC_FORM_C"));
		String smpDocFormGV = OldCommonUtil.safeObjToStr(map.get("SMP_DOC_FORM_G_V"));
		String storPathNm = OldCommonUtil.safeObjToStr(map.get("STOR_PATH_NM"));
		String procFileNm = OldCommonUtil.safeObjToStr(map.get("PROC_FILE_NM"));
		String imgIdxNo = OldCommonUtil.safeObjToStr(map.get("IMG_IDX_NO"));
		int delimiter = '|';
		
		String fileNm = "";
		String singleTIFFList = "";
		String multiTIFFName = "";
		
		try {
			String infDocFormC = OldCommonUtil.getInfCodeType(docFormC);
			
			if(OldCommonUtil.isEmpty(imgIdxNo)) {
				//연동정보가 없는 경우
				fileNm = String.format("%s,%s.tif",infDocFormC, repBprKey); 
			} else {
				fileNm = String.format("%s,%s.tif",infDocFormC, imgIdxNo); 
			}
			
			
			long s_time = System.currentTimeMillis();
			
			dirFileVO.setEDocIdxNo(eDocIdxNo);
			dirFileVO.setImgSer(imgSer);
			
			File pdfFile = OldFileUtil.joinPaths(storPathNm, procFileNm);
			
			String pdfFilePath = pdfFile.getPath();
			String pdfFileName = pdfFile.getName();
			long pdfFileLenath = pdfFile.length();
			
			if(OldFileUtil.exists(pdfFile) == false ) {
				dirFileVO.setConvert_ret(false);
				dirFileVO.setDb_Ret(false);
				
				EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0501", "파일이 없습니다.");
				return dirFileVO;
			}
			
			//pdf 파일사이즈 체크하여 1m 이하면 오류
			if(pdfFile.length() < 1024) {
				dirFileVO.setConvert_ret(false);
				dirFileVO.setDb_Ret(false);
				
				EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0502", "파일 사이즈가 작습니다.");
				return dirFileVO;
			}
			
			//pdf 페이지수 가져오기
			int pdfPageCnt = ImgUtil.getPDFPAgeCount(pdfFile);
			
			if(pdfPageCnt < 1) {
				dirFileVO.setConvert_ret(false);
				dirFileVO.setDb_Ret(false);
				
				EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0503", "페이지가 없습니다.");
				return dirFileVO;
			}
			
			
			//이미지생성경로
			String imgPath = OldFileUtil.joinPaths(pdfFile.getPath(), OldFileUtil.PATH.IMAGE).getPath();
			
			boolean isDir = OldFileUtil.mkdirs(imgPath);
			
			
			//이미지 변환 시작
			
			long sttm = System.currentTimeMillis();
			
			//생성된 페이지수(TIF 개수) 리턴
			int retP2I = ImgUtil.convertPDF2Image(pdfFile, imgPath, pdfPageCnt);
			
			if(retP2I == pdfPageCnt) {
				singleTIFFList = OldFileUtil.getFileListByDelimiter(new File(imgPath), delimiter);
				//String[] tifArray = singleTIFFList.split("\\"+String.valueOf((char)delimiter));
			
				multiTIFFName = OldFileUtil.joinPaths(imgPath, fileNm).getPath();
				
				int ret_multi = ImgUtil.mergeTIFF(singleTIFFList, delimiter, multiTIFFName);
				
				if(ret_multi == 0) {
					//성공하면 DB에 처리단계 및 상태코드 업데이트
					// pdf저장(04) 에서 이미지생성완료(05)
					// 이미지저장상태 1로 set
					int result = DaoUpdate.updateEDocFileMng(imgIdxNo, imgSer, eDocProcSeqV, 1);
				} else {
					dirFileVO.setConvert_ret(false);
					dirFileVO.setDb_Ret(false);
					
					EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0505", "Merge 에러");
					return dirFileVO;
				}
				
			
			} else {
				dirFileVO.setConvert_ret(false);
				dirFileVO.setDb_Ret(false);
				
				EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0504", "생성된 이미지개수가 페이지수와 다름");
				return dirFileVO;
			}
			
			
		} catch (Exception e) {
			log.error(OldSystemUtil.getExceptionLog(e));
			EdsDao.registerErrHis(eDocIdxNo, imgSer, eDocProcSeqV, "0901", "이미지변환 에러");
		}
		return dirFileVO;
		
	}
	
}
