package com.people.img.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
//import com.inzisoft.pdf2image.InziPDF;

public class ImgUtil {
	public static int getPDFPAgeCount(File pdfFile) {
		return getPDFPAgeCount(pdfFile.getPath());
	}
	
	private static int getPDFPAgeCount(String fileName) {
//		return InziPDF.getPDFPageCount(fileName);
		return 0;
	}
	
	/**
	 * pdf를 이미지(tif)로 저장한 후 성공한 페이지수를 리턴
	 * - 성공한 페이지수는 전체 페이지가 아닐 수 있음
	 * 
	 * @param pdfFileName
	 * @param imgOutPath
	 * @param comprate
	 * @return
	 */
	public static int convertPDF2Image(File pdfFile, String imgOutPath, int comprate) {
		return convertPDF2Image(pdfFile.getPath(), imgOutPath, comprate);
	}
	
	private static int convertPDF2Image(String pdfFileName, String imgOutPath, int comprate) {
		//return InziPDF.convertPDF2Image(pdfFileName, imgOutPath, 200, comprate, 4, 34713,0,0);
		return 1;
	}
	
	/**
	 * 성공하면 리턴 0
	 * @param singleTIFFList
	 * @param delimiter
	 * @param outFileName
	 * @return
	 */
	public static int mergeTIFF(String singleTIFFList, int delimiter, String outFileName) {
//		return InziPDF.mergeTIFF(singleTIFFList, delimiter, outFileName);
		return 0;
	}
	
	public static String getPdf2ImgErrMsg(int err) {
		Map<Integer, String> error = new HashMap<Integer, String>();
		
		error.put(0,"성공");
		error.put(-1,"pdf 데이터 생성 실패");
		error.put(-2,"메모리할당 실패");
		error.put(-3,"유효하지 않은 파일 이름");
		error.put(-4,"라이센스가 유효하지 않음");
		error.put(-5,"포인트 값이 null");
		error.put(-6,"DLL 로드 실패");
		error.put(-7,"DLL에서 적절한 함수를 찾지 못함");
		error.put(-8,"파일 열기 실패");
		error.put(-9,"파일크기가 유효하지 않음");
		error.put(-10,"코덱 관련 함수 에러");
		error.put(-11,"파일 생성과정 에러");
		error.put(-12,"pdf 파일 오픈 실패");
		error.put(-13,"페이지번호에 해당하는 페이지가 없음");
		error.put(-14,"pdf 파일 저장 실패");
		error.put(-15,"pdf에서 변환 가능하지 않은 이미지형식");
		error.put(-16,"적절하지 않은 회전값");
		error.put(-17,"적절하지 않은 압축율");
		error.put(-18,"font 데이터를 로드하는 과정 에러");
		error.put(-19,"pdf 이미지 로드 에러");
		error.put(-20,"jpeg 이미지로드 에러");
		error.put(-21,"jpeg200 in TIFF 이미지 로드 에러");
		error.put(-22,"jbig2 in TIFF 이미지 로드 에러");
		error.put(-23,"G4 이미지 로드 에러");
		error.put(-24,"pdf 도큐먼트 참조에러");
		error.put(-25,"pdf 도큐먼트 참조에러");
		error.put(-26,"pdf 페이지 개수 구하는 과정 에러");
		error.put(-27,"dpf 입력이미지 생성과정 에러");
		error.put(-28,"이미지 레퍼런스 index가 유효하지 않음");
		error.put(-29,"이미지 레퍼런스 정보가 유효하지 않음");
		error.put(-30,"pdf 페이지번호가 적절하지 않음");
		error.put(-31,"입력된 pdf 파일이 너무 많아 병합할 수 없음.");
		error.put(-32,"pdf파일 암호로 인한 에러");
		error.put(-33,"pdf페이지 구조 가져오는데 에러");
		error.put(-34,"pdf페이지불러오는데 실패");
		error.put(-35,"pdf로 변환할 이미지 개수가 너무 많습니다.");
		error.put(-36,"pdf 페이지 데이터를 분석하는 실패");
		error.put(-37,"입력받은 사이즈모드가 유효하지 않습니다.");
		error.put(-38,"입력받은 사이즈가 유효하지 않음");
		error.put(-39,"입력받은 마스킹정보가 유효하지 않음");
		error.put(-40,"pdf 데이터 생성 실패");
		error.put(-41,"pdf 변환 오류");
		error.put(-42,"pdf 내부데이터를 HDIB로 변환 중 에러");
		error.put(-43,"HDIB를 이진화중에러");
		error.put(-44,"pdf 내부데이터를 파일로 저장하는 도중 에러");
		error.put(-45,"DPI나  Pixel sizemode가 아니어서 에러");
		error.put(-46,"pdf 생성정보 설정 실패");
		error.put(-47,"pdf 내부페이지생성실패");
		error.put(-48,"pdf를 그리는데 에러");
		error.put(-49,"잘못된 파라미터");
		error.put(-50,"time stamp image 생성실패");
		error.put(-51,"pdf 내부데이터 생성 실패");
		error.put(-52,"pdf 내부페이지에 이미지적용실패");
		error.put(-53,"pdf 내부페이지에 마스킹 적용실패");
		error.put(-54,"pdf 내부페이지에 마스킹 적용실패");
		error.put(-55,"pdf 데이터 찾기 실패");
		error.put(-56,"pdf 주석이 없음");
		error.put(-57,"pdf 주석 추가실패");
		error.put(-58,"pdf 주석 목록 가져오기 실패");
		error.put(-59,"pdf 데이터 삭제 실패");
		error.put(-60,"파일 IO 관련 에러");
		error.put(-61,"pdf 내부페이지에 이미지 적용실패");
		error.put(-62,"pdf 내부페이지에 텍스트 적용실패");
		error.put(-63,"pdf 내부페이지에 펜 데이터 적용실패");
		error.put(-64,"라이센스파일로드 실패");
		error.put(-65,"TIFF 이미지화 작업 실패");
		error.put(-66,"IZT 이미지화 작업 실패");
		error.put(-67,"pdf 객체를 가져오는데 실패");
		error.put(-68,"pdf 키워드 가져오는데 시패");
		error.put(-69,"pdf stream 데이터 가져오는데 시패");
		error.put(-70,"로그분석이 필요합니다.");
		error.put(-71,"dpf 내부뎅터 목록이 비어있음");
		error.put(-72,"Single Strip만 지원");

		if(error.containsKey(err)) {
			return error.get(err);
		} else {
			return "알 수 없는 에러";
		}
		
	}
	
	public static String getMergeTiffErrMsg(int err) {
		Map<Integer, String> error = new HashMap<Integer, String>();
		
		error.put(0,"성공");
		error.put(1,"원본파일이상 - 1");
		error.put(2,"원본파일이상 - 2");
		error.put(3,"libTIFFIO.so 로딩실패");
		error.put(4,"libTIFFIO.so에 필요한 함수가 없음");
		error.put(5,"파일을 병합할 수 없음");
		error.put(6,"원본파일열기실패");
		error.put(7,"결과파일저장실패");
		error.put(89,"TIF Not Found");
		error.put(90,"파라미터 에러");
		error.put(91,"libImageIO.so 로딩실패");
		error.put(92,"libImageIO.so에 필요한 함수가 없음");
		error.put(99,"기타오류");

		if(error.containsKey(err)) {
			return error.get(err);
		} else {
			return "알 수 없는 에러";
		}
		
	}
	
	
	
	
	
	
}
