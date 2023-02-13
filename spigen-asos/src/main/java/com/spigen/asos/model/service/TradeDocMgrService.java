package com.spigen.asos.model.service;

import java.util.List;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;

public interface TradeDocMgrService {
	
	public List<DocMgrVO> selectBankInfoList(FileMgrVO fileVO) throws Exception;

	public int insertBankInfo(FileMgrVO fileVO) throws Exception;

	public int updateBankInfo(FileMgrVO fileVO) throws Exception;
	
	public int insertSendHis(FileMgrVO fileVO) throws Exception;

	public List<FileMgrVO> selectfaxSendHisQry(FileMgrVO fileVO) throws Exception;

	public List<FileMgrVO> selectFileMgrListQry(FileMgrVO fileVO) throws Exception;

	public List<FileMgrVO> selectTaxFileMgrListQry(FileMgrVO fileVO) throws Exception;
}
