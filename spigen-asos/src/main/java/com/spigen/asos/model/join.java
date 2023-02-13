package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm

import java.sql.Date;//얘 아니면 util임
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table; //보셈

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter

public class join {
	private String wkonum;
	private String assycd;
	private String creusr;
	private String ospser;
	private String qtyord;
	private String credat;
	private String sddate;
	private String wktype;
	private String remark;
	private String memo01;//패킹가이드
}