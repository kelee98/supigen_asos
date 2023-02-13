package com.spigen.asos.model.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.exList;
import com.spigen.asos.model.exableList;
import com.spigen.asos.model.service.ProdDocMgrService;
import com.spigen.asos.model.service.exWatingListService;

@Service("exWatingListService")
public class exWaitingListServiceImpl implements exWatingListService{
	@Resource(name = "exWatingListDAO")
	exWatingListDAO exWatingListDAO;
	
	@Override
	public List<exableList> selectEXableList(){
		return exWatingListDAO.selectEXABLEList();
	}
	@Override
	public List<exList> selectEXList(){
		return exWatingListDAO.selectEXList();
	}
}
