package com.spigen.asos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmMgrVO {

	private String gnrCd; //공통코드
	private String gnrNm; //공통코드명
	private String length; //코드길이
	private String useYn; //사용여부
	private String dtlCd; //상세코드
	private String preDtlCd; //상세코드
	private String dtlNm; //상세코드명
	private String dtlCts; //상세코드내용
	private String caption; //비고

	private String corpNum ="1208736593"; //사업자번호
}
