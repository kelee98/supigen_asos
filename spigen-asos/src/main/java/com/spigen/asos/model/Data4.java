package com.spigen.asos.model;
//persistence - 데이터를 생성한 프로그ㅐㅁ이 종료되더라도 사라지지않는 데이터의 특성. persistence framework 로 hibernate,mybatis를 사용
//mapper vs orm
import javax.persistence.Column;
import javax.persistence.Entity;//javax.persistence, jpa가 뭔지보거라
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table; //보셈

import org.springframework.util.Assert;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity //테이블과 매핑한다고 JPA에게 알려준다.- db테이블과 일대일 매칭되는 객체단위
//@Table(name="JW")
@Getter//@Getter, @Setter, @ToString 등 기본적으로 필요한 함수를 자동 생성 및 오버라이딩
@NoArgsConstructor

public class Data4 {
	@Id //primary key
	@GeneratedValue(strategy=GenerationType.IDENTITY) //auto increment
	private Long no;
	@Column(length=100)
    private String name;//칼럼수 default=255 -> 줄이려면 어케함? annotation 칼럼해서 할수있음
    private int age;
    private String description;
    
    @Builder
    public Data4(String name, String description) {
    	Assert.hasText(name, "name must not be empty");
        Assert.hasText(description, "description must not be empty");
    	//안전한 객체 생성 패턴으로 생성했을 경우는 객체 생성이 Assert으로 객체 생성이 진행되지 x.
    	//필요한 값이 없는 상태에서 객체를 생성하면 이후 작업에서 예외가 발생 (컨트롤러에서 유효성 검사하는 이유와 동일)
    	this.name= name;
    	this.description = description;
    }
}