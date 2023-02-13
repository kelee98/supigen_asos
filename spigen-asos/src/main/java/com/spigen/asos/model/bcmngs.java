package com.spigen.asos.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="bcmng")
public class bcmngs {
	@Id //primary key
	@Column
	private String assycd;   //완제품 코드   table: bcmng
	
	
	@Column
	private String cornum;   //임가공업체 코드   table: bcmng
}
