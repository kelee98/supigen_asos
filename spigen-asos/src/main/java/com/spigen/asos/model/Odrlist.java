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

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="wkord")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter
@ToString
@NoArgsConstructor
public class Odrlist {
	@Id //primary key
	private String wkonum;//작업지시번호
    private String orddat;//작업지시일자
    private String worder;//지시자
    private String wkstat;//작업상태
    private String orisrc;//원소스구분(1=수기입력, 2=ERP;로부터받음)
    private String remark;//비고
    private String cornum;//임가공업체코드
    private String credat;//creation date
    private String cretim;//creation time
    private String creusr;//create by
    private String lmodat;//last modified date
    private String lmotim;//last modified time
    private String lmousr;//last modified by
    private String wktype;//작업 유형

  /*  @OneToMany(mappedBy="odrlist") //일대다 단방향 매핑
    @JoinColumn(name="wkonum")
    //private Prdlist prdlist;
    private List<Prdlist> prdlists = new ArrayList<>();*/
}