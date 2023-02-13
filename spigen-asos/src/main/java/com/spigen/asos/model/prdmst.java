package com.spigen.asos.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="prd_mst")
public class prdmst {            //상품 마스터
	@Id //primary key
	private BigInteger id;
	@Column
	private String MATNR;       //자재 유형
	@Column
	private String komtx;       //자내내역(완제품 명)
	
	
}
