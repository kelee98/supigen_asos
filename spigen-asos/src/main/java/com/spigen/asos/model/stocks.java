package com.spigen.asos.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name="stock")
public class stocks {
	@Id //primary key
	private String mstoky;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY) //auto increment
	private Long idx;
	
	@Column
	private String kndstk;  //재고 종류(구분) table: stock
	
	
	@Column
	private String assycd;   //완제품 코드   table: stock
	
	@Column
	private String deleyn;  // 취소여부    			table:stock
	
	@Column
	private int qtyitm;   // 재고수량      table:stock
	
	
	
	

}
