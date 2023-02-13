package com.spigen.asos.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Data
@AllArgsConstructor

public class inList {

	
	private String kndstk;  //재고 종류(구분) table: stock
	

	private String bu_sort1;   //업체 코드      table: wkord
	
	
	private String assycd;   //완제품 코드   table: stock
	

	private String komtx;     //완제품 명    table:ordtype
	

	private int qtyitm;   // 재고수량      table:stock
	
	
	

	}
