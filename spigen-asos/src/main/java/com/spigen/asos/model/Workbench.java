package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm



import javax.persistence.Column;
import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table; //보셈


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
@Table(name="Workbench")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@Setter
@ToString
@NoArgsConstructor
public class Workbench {
	@Id //primary key
	private String bchno;
	private long asnqnt;
	
	//@OneToMany(mappedBy = "prdlist")
    //private Collection<Odrlist> odrlist;
	//private List<Odrlist> odrlists = new ArrayList<Odrlist>();
}