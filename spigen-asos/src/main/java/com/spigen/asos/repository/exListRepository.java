package com.spigen.asos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spigen.asos.model.exableList;

import com.spigen.asos.model.stbot;

public interface exListRepository extends JpaRepository<stbot, Long>{
	List<stbot> findAll();
	List<stbot> findByWkokey(String wkokey);
	
	
	//exList findByAsn(Long Asn);
	//exList findByIdx(Long Idx);
}
