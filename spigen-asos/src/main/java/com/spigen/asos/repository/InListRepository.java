package com.spigen.asos.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spigen.asos.model.inList;
import com.spigen.asos.model.inListInterface;
import com.spigen.asos.model.stocks;

//JpaRepository 에서 기본적인 crud 제공, 매개변수로 도메인클래스와 키타입을 기술
public interface InListRepository extends JpaRepository<stocks, String>{
	  List<stocks> findByAssycd(String keyword);
	 
		 @Query("SELECT distinct b.kndstk as kndstk  ,b.assycd as assycd  ,a.komtx as komtx ,b.qtyitm as qtyitm, d.bu_sort1 as bu_sort1 "+
 	     		"FROM stocks b "+
 	     		"INNER JOIN prdmst a ON a.MATNR = b.assycd "+
 	     		"INNER JOIN bcmngs c ON c.assycd = b.assycd "+
 	     		"INNER JOIN Vdrlist d ON d.lifnr=c.cornum " +
 	     		"WHERE c.cornum LIKE CONCAT('%',:keyword,'%')"+
 	     		"AND b.assycd LIKE CONCAT('%',:code,'%')"
 	     		)
		 List<inListInterface> joininlist(String keyword,String code);
		 
		 @Query("SELECT distinct b.kndstk as kndstk  ,b.assycd as assycd  ,a.komtx as komtx ,b.qtyitm as qtyitm, d.bu_sort1 as bu_sort1 "+
	 	     		"FROM stocks b "+
	 	     		"INNER JOIN prdmst a ON a.MATNR = b.assycd "+
	 	     		"INNER JOIN bcmngs c ON c.assycd = b.assycd "+
	 	     		"INNER JOIN Vdrlist d ON d.lifnr=c.cornum " +
	 	     		"WHERE b.assycd IN :matnrArr"
	 	     		)
			 List<inListInterface> findassninlist(String[] matnrArr);
			 @Query("SELECT distinct b.kndstk as kndstk  ,b.assycd as assycd  ,a.komtx as komtx ,b.qtyitm as qtyitm, d.bu_sort1 as bu_sort1 "+
					 "FROM stocks b "+
					 "INNER JOIN prdmst a ON a.MATNR = b.assycd "+
					 "INNER JOIN bcmngs c ON c.assycd = b.assycd "+
					 "INNER JOIN Vdrlist d ON d.lifnr=c.cornum "
					 )
			 List<inListInterface> findassninlistAll(String[] matnrArr);
		 
		 @Query("SELECT distinct b.kndstk as kndstk  ,b.assycd as assycd  ,a.komtx as komtx ,b.qtyitm as qtyitm, d.bu_sort1 as bu_sort1 "+
	 	     		"FROM stocks b "+
	 	     		"INNER JOIN prdmst a ON a.MATNR = b.assycd "+
	 	     		"INNER JOIN bcmngs c ON c.assycd = b.assycd "+
	 	     		"INNER JOIN Vdrlist d ON d.lifnr=c.cornum " 
	 	     		)
		 List<inListInterface> alljoininlist();
}