package com.spigen.asos.model.service;

import java.util.List;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.exList;
import com.spigen.asos.model.exableList;

public interface exWatingListService {
	public List<exableList> selectEXableList() throws Exception;
	public List<exList> selectEXList()throws Exception;
}
