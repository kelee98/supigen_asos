package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm

import java.sql.Date;

import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.Id;
import javax.persistence.Table; //보셈

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="vnd_mst")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter
@NoArgsConstructor
public class Vdrlist {
	@Id //primary key
	private String lifnr;
    private String bstae;
    private String bu_sort1;
    private String bu_sort2;
    private String ekorg;
    private String ekotx;
    private String inco1;
    private String inco2;
    private String j_1kfrepre;
    private String j_1kftbus;
    private String j_1kftind;
    private String kalsk;
    private String ktokk;
    private String land1;
    private String landx;
    private String loevm;
    private String name1;
    private String sele;
    private String sortl;
    private String stcd2;
    private String stras;
    private String telf1;
    private String telfx;
    private String text1;
    private String text1_1;
    private String text2;
    private String text2_1;
    private String text2_2;
    private String text3;
    private String text3_1;
    private String text3_2;
    private String text3_3;
    private String text4;
    private String text5;
    private String text5_1;
    private String text5_2;
    private String text6;
    private String text6_1;
    private String text6_2;
    private String text7;
    private String text8;
    private String txt30;
    private String waers;
    private String zterm;
    private Date regdate;
    private Date updatedate;
    
}