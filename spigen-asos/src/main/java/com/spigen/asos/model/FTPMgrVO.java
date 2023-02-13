package com.spigen.asos.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FTPMgrVO {
	
	private String fileNm;
	private String filePath;
	private String fileType;
	private String qryYear;
	private String qryQuarter;
	private String docType;
	private String server = "106.242.30.58";
	private String nationA;
	private String nationC;
	
	//로컬
	//private String server = "172.16.3.61"; 
	private int port = 5757; 
	private String id = "spigenweb"; 
	private String password = "Spigen1234!"; 
}
