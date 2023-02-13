package com.spigen.asos.model.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.exList;
import com.spigen.asos.model.exableList;

@Repository("exWatingListDAO")
public class exWatingListDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;
	
 
	public List<exableList> selectEXABLEList() {
		return sqlSession.selectList("mappers.exListMapper.selectExableList");
	}
	public List<exList> selectEXList() {
		return sqlSession.selectList("mappers.exListMapper.selectExList");
	}
}

