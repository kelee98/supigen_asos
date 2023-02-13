package com.spigen.asos.model;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocMgrVO {

	private String mvgr2; //제조사
	private String mvgt2; //제조사이름
	private String mvgr3; //기종
	private String mvgt3; //기종이름
	private String normt; //모델명
	private String sku; //상품sku
	private String comnYn; //공통여부
	private String req; //서류 필수 여부
	private String sReq; //시방서 필수 여부
	private String aReq; //승인원 필수 여부
	private String dReq; //도안 필수 여부
	private String finYn; //완료여부
	private String matnr; //SKU
	private List<DocMgrVO> subList; //SKU List	
	private String fileNm; //파일명
	private String path; //파일경로
	private String fileNmS; //파일명
	private String pathS; //파일경로
	private String fileNmA; //파일명
	private String pathA; //파일경로
	private String fileNmD; //파일명
	private String pathD; //파일경로
	private String fileTypeS; //파일경로1
	private String fileTypeA; //파일경로2
	private String fileTypeD; //파일경로2
	private String fileType; //파일경로
	private String seqS; //시퀀스_시방서
	private String seqA; //시퀀스_승인원
	private String seqD; //시퀀스_도안
	private String seq; //시퀀스
	private String rownum; //순번
	private List<String> qryLs;
	

	private String stlal; //대체 BOM
	private String maktx; //자재내역 
	private String enmtx; //자재내역
	private String alevel; //상위레벨
	private String clevel; //배치 사용처 파일 재구성 레벨
	private String zpstau; //품목상태
	private String zstoc; //재고등급
	private String imtart; //자재 유형
	private String idnrk; //자재 번호
	private String cstlal; //배치 번호
	private String ztext; //비고
	private String zmngko; //구성수량
	private String bmeng; //확인된 수량
	private String menge; //구성부품수량
	private String bbmeng; //확인된 수량
	private String meins; //기본 단위
	private String zpostp; //조달구분
	private String imatkl; //자재 그룹
	private String iwgbez; //자재 그룹 내역
	private String hmatnr; //상위일련번호자재
	private String hstlal; //대체번호
	private String stktx; //대체 BOM 텍스트
	private String mtart; //자재 유형
	private String matkl; //자재 그룹
	private String wgbez; //자재 그룹 내역
	private String dispo; //MRP 관리자
	private String dsnam; //MRP 관리자이름
	private String prdha; //제품 계층구조
	private String mvgr1; //브랜드
	private String mvgt1; //내역
	private String mvgr4; //색상
	private String mvgt4; //내역
	private String sanko; //엔지니어링 관련 품목
	private String sanfe; //생산 관련 품목
	private String sanka; //원가계산 관련 지시자
	private String andat; //레코드 생성일
	private String sastu; //텍스트필드 아이콘
	
	private String  ersda; //생성일
	private String  ntgew; //순 중량
	private String  gewei; //중량 단위
	private String  ean11; //국제 상품 번호(EAN/UPC)
	private String  laeng; //길이
	private String  breit; //너비
	private String  hoehe; //높이
	private String  zwon;  //원산지정보
	private String  company; //회사코드
	private String  charg;   //배치 번호
	private String  zinbq;   //INBOX수량
	private String  zoubq;   //OUTBOX수량
	private String  vkorg;   //영업 조직
	private String  vtweg;   //유통 경로
	private String  zpacs;   //패키지사양
	private String  pdim;    //패키지 Dim
	private String  rdim;    //원자재 Dim
	
	private int cnt;
	private String qryFlag; //조회 기준
	private String qryValue; //조회 값
	private String docType; //서류유형(S:시방서)
	private String docTypeNm; //서류유형(S:시방서)
	private String mrgYn; //병합여부
	private String chgCd; //변경코드(C:등록/U:수정/D:삭제)
	
	private String vtext; //자재코드
	private String komtx; //자재내역
	private String remarks; //비고
	private String crtrId; //생성자
	private String [] normtArr;
	private String delYn;
	private String chkYn;
	
	private String vdrNm; //벤더명
	private String lifnr; //벤더코드
	
	//페이지
	private int pageIndex = 1;
	private int pageUnit = 15;
	private int pageSize = 10;
	private int firstIndex = 0;
	private int lastIndex = 0;
	private int recordCountPerPage = 15;
	@Override
	public String toString() {
		return "DocMgrVO [mvgr2=" + mvgr2 + ", mvgt2=" + mvgt2 + ", mvgr3=" + mvgr3 + ", mvgt3=" + mvgt3 + ", normt="
				+ normt + ", sku=" + sku + ", comnYn=" + comnYn + ", req=" + req + ", sReq=" + sReq + ", aReq=" + aReq
				+ ", dReq=" + dReq + ", finYn=" + finYn + ", matnr=" + matnr + ", subList=" + subList + ", fileNm="
				+ fileNm + ", path=" + path + ", fileNmS=" + fileNmS + ", pathS=" + pathS + ", fileNmA=" + fileNmA
				+ ", pathA=" + pathA + ", fileNmD=" + fileNmD + ", pathD=" + pathD + ", fileTypeS=" + fileTypeS
				+ ", fileTypeA=" + fileTypeA + ", fileTypeD=" + fileTypeD + ", fileType=" + fileType + ", seqS=" + seqS
				+ ", seqA=" + seqA + ", seqD=" + seqD + ", seq=" + seq + ", rownum=" + rownum + ", qryLs=" + qryLs
				+ ", stlal=" + stlal + ", maktx=" + maktx + ", enmtx=" + enmtx + ", alevel=" + alevel + ", clevel="
				+ clevel + ", zpstau=" + zpstau + ", zstoc=" + zstoc + ", imtart=" + imtart + ", idnrk=" + idnrk
				+ ", cstlal=" + cstlal + ", ztext=" + ztext + ", zmngko=" + zmngko + ", bmeng=" + bmeng + ", menge="
				+ menge + ", bbmeng=" + bbmeng + ", meins=" + meins + ", zpostp=" + zpostp + ", imatkl=" + imatkl
				+ ", iwgbez=" + iwgbez + ", hmatnr=" + hmatnr + ", hstlal=" + hstlal + ", stktx=" + stktx + ", mtart="
				+ mtart + ", matkl=" + matkl + ", wgbez=" + wgbez + ", dispo=" + dispo + ", dsnam=" + dsnam + ", prdha="
				+ prdha + ", mvgr1=" + mvgr1 + ", mvgt1=" + mvgt1 + ", mvgr4=" + mvgr4 + ", mvgt4=" + mvgt4 + ", sanko="
				+ sanko + ", sanfe=" + sanfe + ", sanka=" + sanka + ", andat=" + andat + ", sastu=" + sastu + ", ersda="
				+ ersda + ", ntgew=" + ntgew + ", gewei=" + gewei + ", ean11=" + ean11 + ", laeng=" + laeng + ", breit="
				+ breit + ", hoehe=" + hoehe + ", zwon=" + zwon + ", company=" + company + ", charg=" + charg
				+ ", zinbq=" + zinbq + ", zoubq=" + zoubq + ", vkorg=" + vkorg + ", vtweg=" + vtweg + ", zpacs=" + zpacs
				+ ", pdim=" + pdim + ", rdim=" + rdim + ", cnt=" + cnt + ", qryFlag=" + qryFlag + ", qryValue="
				+ qryValue + ", docType=" + docType + ", docTypeNm=" + docTypeNm + ", mrgYn=" + mrgYn + ", chgCd="
				+ chgCd + ", vtext=" + vtext + ", komtx=" + komtx + ", remarks=" + remarks + ", crtrId=" + crtrId
				+ ", normtArr=" + Arrays.toString(normtArr) + ", delYn=" + delYn + ", vdrNm=" + vdrNm + ", lifnr="
				+ lifnr + ", pageIndex=" + pageIndex + ", pageUnit=" + pageUnit + ", pageSize=" + pageSize
				+ ", firstIndex=" + firstIndex + ", lastIndex=" + lastIndex + ", recordCountPerPage="
				+ recordCountPerPage + "]";
	}

	

}
