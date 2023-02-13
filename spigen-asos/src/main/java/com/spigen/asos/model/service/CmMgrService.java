package com.spigen.asos.model.service;

import java.util.List;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;

public interface CmMgrService {

	public List<CmMgrVO> selectGnrCdList(CmMgrVO vo) throws Exception;

	public List<DocMgrVO> selectGnrCdDetailList(CmMgrVO vo) throws Exception;

	public int insertGnrCdRgt(CmMgrVO vo) throws Exception;

	public int insertGnrCdDetailRgt(CmMgrVO vo) throws Exception;

	public CmMgrVO selectGnrCdQry(CmMgrVO vo) throws Exception;

	public int updateGnrCdUpd(CmMgrVO vo) throws Exception;

	public CmMgrVO selectGnrCdDetailQry(CmMgrVO vo) throws Exception;

	public int updateGnrCdDetailUpd(CmMgrVO vo) throws Exception;

	public List<CmMgrVO> selectProdInfoList(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> selectVdrList(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> vndrBySKUList(DocMgrVO vo) throws Exception;

	public int insertSKUByVndr(DocMgrVO vo) throws Exception;

	public int deleteSKUByVndr(DocMgrVO vo) throws Exception;

	public List<FileMgrVO> selectClntList(FileMgrVO vo) throws Exception;

}
