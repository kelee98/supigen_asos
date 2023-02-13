package com.spigen.asos.model;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileMgrVO {

	private String fileNm;
	private String fileExt;
	private File downFile;
	private String filePath;
	private String fileType;
	private List<String> fileTypeArr;
	private String[] chkFiles;
	private MultipartFile [] uploadfiles;
	private int sendPgs;
	private int sendPee;
	private String docType;
	private String docNo; //조회기준 번호
	private String blNo; //조회기준 번호
	private String ciNo; //조회기준 번호
	private String sendYn; //전송여부
	private String sendYnNm; //전송여부명
	private String resendYn; //재전송여부
	private String qryBfr; //조회기간 1
	private String qryAftr; //조회기간 2
	private String crtDt; //생성일자
	private String rgtId; //등록자ID
	private String rgtDtm; //등록일자
	private String updDtm; //수정일자
	private String updId; //수정자ID
	private String laufi; //이체ID
	private String laufd; //프로그램 실행일
	private String gjahr; //회계연도
	private String belnr; //회계 전표 번호(발생전표)
	private String vblnr; //지급전표번호
	private String eviSeq; //증빙일련번호
	private String zbukr;
	private String name1; //거래처

	private String fileAppnr; //파일
	private String fileText; //파일
	private String fileDate; //파일
	private String fileSize; //파일 사이즈
	
	
	
	private String bankNm; //은행명
	private String bankBrch; //은행지점명
	private String defaultFax; //기본팩스
	private String faxNum; //팩스번호
	private String faxNum1; //팩스번호1
	private String mngrNm; //담당자명
	private String mngrNum; //담당자 전화번호
	private String mngrNum2; //담당자 전화번호(내선)
	private String useYn; //사용여부
	private String useYnNm; //사용여부명
	private String seq; //고유번호
	private String fileSeq; //파일 고유번호
	private String reqNum; //접수번호
	private String sendDt; //전송일시
	private int sendState; //전송상태
	private int sendResult; //전송결과
	
	private String brcd; //지점코드
	private String remtRscd1; //송금사유코드
	private String dracNo; //은행계정번호
	private String dracBicNm; //출금은행명
	private String rcvrEngNm; //수취인
	private String rcvrAcno; //입금계좌번호
	private String pybkAdr; //입금은행명
	private String ebnkFamt; //금액
	private String remtCurcd; //송금통화코드
	private String reqURL;
	
	//수출입 선적서류-세무서
	private String vbeln; //납품
	private String aubel; //참조 문서의 문서 번호
	private String tknum; //선적번호
	private String zreqty; //결제유형
	private String bstnk; //참조 고객
	private String zexdno; //선적서류 정형화 번호
	private String zdtypeCi; //서류유형
	private String zfileCi; //파일명
	private String zexidCi; //수출문서ID
	private String zcivno; //Invoice No
	private String netwrIv; //전표 통화의 오더 품목 정가
	private String zexfno; //FD 관리번호
	private String zexbno; //B/L관리번호
	private String zdtypeBl; //서류유형
	private String zfileBl; //파일명
	private String zexidBl; //수출문서ID
	private String zhblno; //House B/L No.
	private String zmblno; //Master B/L No.
	private String zobdt; //On Board Date,Sailing on or about
	private String zexdt; //수출이행일
	private String matnr; //자재 번호
	private String arktx; //판매 오더 품목에 대한 내역
	private String zexcno; //수출통관관리번호
	private String zedsno; //신고수리번호
	private String zedsdt; //신고수리일자
	private String wadatList; //실제 자재 이동일
	private String wadat; //계획된 자재 이동일
	private String vbelnK; //청구 문서
	private String fkdat; //청구일
	private String fkimg; //실제 대금청구 수량
	private String vrkme; //판매 단위
	private String waerk; //SD 문서 통화
	private String netwr; //문서통화의 청구품목 정가
	private String zfwdr; //Forwarder
	private String name1F; //이름 1
	private String kunnrR; //판매처
	private String name1R; //이름 1
	private String kunnrN; //납품처
	private String name1N; //이름 1
	private String inco1; //인도 조건(파트 1)
	private String inco2; //인코텀스(파트 2)
	private String zterm; //지급 조건 키
	private String bukrs; //회사 코드
	private String bsartT; //수출유형
	
	private String fileTypeCI; //파일형식_CI
	private String fileTypeBL; //파일형식_BL
	private String docTypeCI; //서류종류_CI
	private String docTypeBL; //서류종류_BL
	private String fileNmCI; //파일명_CI
	private String fileNmBL; //파일명_CI
	
	private String qryType; //조회타입
	private String qryQBfr; //분기별
	private String qryQAftr; //분기별
	private String qryMBfr; //월별
	private String qryMAftr; //월별
	
	private String srchCon; //서치컨디션
	private String chgCd; //변경구분
	private List<String> qryLs;

	//페이지
	private int cnt; //총건수
	private int pageIndex = 1;
	private int pageUnit = 20;
	private int pageSize = 10;
	private int firstIndex = 0;
	private int lastIndex = 0;
	private int recordCountPerPage = 15;
	
	
}
