package com.spigen.asos.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spigen.asos.model.Odrlist;
import com.spigen.asos.model.aitem;
import com.spigen.asos.model.odrjInterface;

public interface OdrListRepository extends JpaRepository<Odrlist, Long>{
	
	List<Odrlist> findByCornum(String cornum);
	
	@Transactional
	void deleteByWkonum(String to);
	
	@Query("SELECT w.wkonum as wkonum,a.assycd as assycd,a.ospser as ospser,a.qtyord as qtyord, a.creusr as creusr, a.credat as credat,a.sddate as sddate,a.wktype as wktype"
			+ " FROM aitem a"
			+ " INNER JOIN Odrlist w ON a.wkonum = w.wkonum"
			+ " WHERE w.cornum LIKE CONCAT('%',:keyword,'%')")
	List<odrjInterface> odrjlist(String keyword);


}