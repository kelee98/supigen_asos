package com.spigen.asos.model;


import lombok.Getter;
import lombok.Setter;//DB연결+getter setter 쉽게
import lombok.ToString;

@Getter
@Setter
@ToString
public class Data {
    
    private String title;
    private String description;
    private int value;
	
    

}
