package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm

import java.sql.Date;//얘 아니면 util임
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table; //보셈

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="aitem")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter
@ToString
@NoArgsConstructor
@IdClass(AitemId.class)
public class aitem {

//	@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private String wkonum;//작업지시번호
    @Id
    private String assycd;
    private int ospser;
    private String reqseq;//
    private String orisrc;
    private int qtyord;//
    private int qtyqta;//
    private String wkstat;
    private String wkedyn;
    private String remark;
    private String credat;
    private String cretim;
    private String creusr;
    private String lmodat;
    private String lmotim;
    private String lmousr;
    private int intoyn;
    private String sddate;
    private String wktype;
    private String inwhsq;
    private String corprd;
    private String prddat;
    private String oripla;
    private int oprice;
    private int untseq;
    private int stdunt;
    private String wordky;
    private String wrkopt;
    private String memo01;
    private String memo02;
    private String iscancel;
    private String bomseq;

    

  /*  @OneToMany(mappedBy="odrlist") //일대다 단방향 매핑
    @JoinColumn(name="wkonum")
    //private Prdlist prdlist;
    private List<Prdlist> prdlists = new ArrayList<>();*/
}