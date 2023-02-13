package com.spigen.asos.model.service;

import java.util.List;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.SheetMgrVO;

public interface ProdDocMgrService {

	public List<DocMgrVO> selectHeaderList(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectDetailList(DocMgrVO docVO) throws Exception;

	public int insertRqrdDocs(DocMgrVO docVO) throws Exception;
	
	public int deleteRqrdDocs(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectSDocMgrList(DocMgrVO docVO) throws Exception;
	
	public int selectSDocMgrListCnt(DocMgrVO docVO) throws Exception;
	
	public List<DocMgrVO> selectCmQryList(String sqlId, DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectCntSkuList(DocMgrVO docVO) throws Exception;
	
	public int insertSDocFile(DocMgrVO docVO) throws Exception;

	public FileMgrVO selectFilePath(DocMgrVO docVO) throws Exception;

	public int selectCntRqrdDocs(DocMgrVO vo) throws Exception;

	public int updateRqrdDocs(DocMgrVO vo) throws Exception;

	public int selectCntRqrdDocsSKU(DocMgrVO vo) throws Exception;

	public int insertRqrdDocsSKU(DocMgrVO vo) throws Exception;

	public int updateRqrdDocsSKU(DocMgrVO vo) throws Exception;

	public DocMgrVO selectMasterInfo(DocMgrVO vo) throws Exception;
	
	public DocMgrVO selectBomMasterInfo(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> selectAttachedFileList(DocMgrVO vo) throws Exception;

	public int updateProdDocFile(DocMgrVO docVO) throws Exception;

	public int deleteProdDoc(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectBomMgrList(DocMgrVO vo) throws Exception;

	public int selectBomMgrListCnt(DocMgrVO vo) throws Exception;

	public int insertBomDocFile(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectAttachedBomFileList(DocMgrVO vo) throws Exception;

	public int updateBomDocFile(DocMgrVO docVO) throws Exception;

	public int deleteBomDoc(DocMgrVO docVO) throws Exception;

	public int insertProdDocFileChgHis(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectBomInfoList(DocMgrVO vo) throws Exception;

	public int selectBomInfoListCnt(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> selectProdInfoList(DocMgrVO vo) throws Exception;

	public int selectProdInfoListCnt(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> prodDocMasterMtrList(DocMgrVO vo) throws Exception;

	public int selectCntRqrdDocsMtr(DocMgrVO vo) throws Exception;

	public int insertRqrdDocsMtr(DocMgrVO vo) throws Exception;

	public int updateRqrdDocsMtr(DocMgrVO vo) throws Exception;

	public List<DocMgrVO> selectProcDocMgrListModel(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectProcDocMgrListSKU(DocMgrVO docVO) throws Exception;

	public int selectProcDocMgrListSKUCnt(DocMgrVO docVO) throws Exception;

	public List<DocMgrVO> selectBomInfoListR(DocMgrVO vo) throws Exception;
	
	public int selectBomInfoListRCnt(DocMgrVO vo) throws Exception;

	public List<SheetMgrVO> selectBomInfoList2(DocMgrVO vo);


}
