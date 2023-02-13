package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table; //보셈

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="pwrst")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter
@ToString
@NoArgsConstructor
//@IdClass(Prdlist.PrdlistKey.class)
public class Prdlist {
	@Id //primary key
	private String wkonum;
	/*
	@Id 
	private String assycd;
	
    @EqualsAndHashCode
    @Embeddable
    static class PrdlistKey implements Serializable {
    	private String wkonum;
    	private String assycd;
    }*/
	
	private String assycd;
	private int ospser;
	private String cornum;
	private String wkbnum;
	private int wrkseq;
	private int qtyend;
	private String endwyn;
	private String credat;
	private String cretim;
	private String creusr;
	private String lmodat;
	private String lmotim;
	private String lmousr;

    /*@ManyToOne
    @JoinColumn(name="wkonum", nullable=false)
    private Odrlist odrlist;*/
}