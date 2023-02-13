package com.spigen.asos.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="stbot")
public class stbot {
	
	
	@Id
	private String wkokey;
	
	@Column
	private String asndky;  // asn 번호                	table: stbot      (출고 시간 ,날자 작성 시에만 생성)     table: stbot
	
	@Column
	private String asndit;  // asn item 번호     	table:stbot  (출고 시간 ,날자 작성 시에만 생성) table:stbot
	
	@Column
	private String otbxlb;   // 아웃box번호  		table: stock
	
	@Column
	private String qtyitm;   // 재고수량         		table:stock
	
	@Column
	private String outdat;    // 출고일자   			table:stbot
	
	@Column
	private String outtim;    //  출고 시간     		table:stbot
	
	@Column
	private String qtyout;    //  출고 수량    		table:stbot
	
	//마감여부?
	
	@Column
	private String outayn;   // 출고 완료 여부 		table:stbot       (출고여부)
}
