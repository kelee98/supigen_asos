package com.spigen.asos.model;

public interface inListInterface {
    String getKndstk();  //재고 종류(구분) table: stock
	

    String getBu_sort1();   //업체 코드      table: wkord
	

	String getAssycd();   //완제품 코드   table: stock
	

	String getKomtx();     //완제품 명    table:ordtype
	

	Integer getQtyitm();   // 재고수량      table:stock
}
