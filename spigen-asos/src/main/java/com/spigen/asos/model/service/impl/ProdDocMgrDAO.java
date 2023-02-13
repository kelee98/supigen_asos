package com.spigen.asos.model.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.SheetMgrVO;

@Repository("prodDocMgrDAO")
public class ProdDocMgrDAO  {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
 
	public List<DocMgrVO> selectHeaderList(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectHeaderList", docVO);
	}


	public List<DocMgrVO> selectDetailList(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectDetailList", docVO);
	}

	public int insertRqrdDocs(DocMgrVO docVO) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertRqrdDocs", docVO);
	}
	
	public int deleteRqrdDocs(DocMgrVO docVO) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.deleteRqrdDocs", docVO);
	}

	public List<DocMgrVO> selectSDocMgrList(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectSDocMgrList", docVO);
	}
	
	public int selectSDocMgrListCnt(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectSDocMgrListCnt", docVO);
	}
	
	public List<DocMgrVO> selectSDocMgrSubList(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectSDocMgrSubList", docVO);
	}
	
	public List<DocMgrVO> selectCmQryList(String sqlId, DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper." + sqlId, docVO);
	}
	
	public List<DocMgrVO> selectCntSkuList(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectCntSkuList", docVO);
	}

	public int insertSDocFile(DocMgrVO docVO) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertSDocFile", docVO);
	}

	public int updateSDocFile(DocMgrVO docVO) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateSDocFile", docVO);
	}
	
	public int insertProdDocFileChgHis(DocMgrVO docVO) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertProdDocFileChgHis", docVO);
	}
	
	public String selectFinYn(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectFinYn", docVO);
	}

	public FileMgrVO selectFilePath(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectFilePath", docVO);
	}

	public int selectCntRqrdDocs(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectCntRqrdDocs", vo);
	}

	public int updateRqrdDocs(DocMgrVO vo) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateRqrdDocs", vo);
	}

	public int selectCntRqrdDocsSKU(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectCntRqrdDocsSKU", vo);
	}

	public int insertRqrdDocsSKU(DocMgrVO vo) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertRqrdDocsSKU", vo);
	}


	public int updateRqrdDocsSKU(DocMgrVO vo) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateRqrdDocsSKU", vo);
	}

	public DocMgrVO selectMasterInfo(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectMasterInfo", vo);
	}
	
	public DocMgrVO selectBomMasterInfo(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectBomMasterInfo", vo);
	}


	public List<DocMgrVO> selectAttachedFileList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectAttachedFileList", vo);
	}


	public int updateProdDocFile(DocMgrVO docVO) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateProdDocFile", docVO);
	}

	public int deleteProdDoc(DocMgrVO docVO) {
		return sqlSession.update("mappers.ProdDocMgrMapper.deleteProdDoc", docVO);
	}

	public List<DocMgrVO> selectBomMgrList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectBomMgrList", vo);
	}

	public int selectBomMgrListCnt(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectBomMgrListCnt", vo);
	}

	public int insertBomDocFile(DocMgrVO vo) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertBomDocFile", vo);
	}

	public int updateBomDocFile(DocMgrVO docVO) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateBomDocFile", docVO);
	}

	public int insertBomDocFileChgHis(DocMgrVO docVO) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertBomDocFileChgHis", docVO);
	}

	public String selectFinYnBom(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectFinYnBom", docVO);
	}

	public List<DocMgrVO> selectAttachedBomFileList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectAttachedBomFileList", vo);
	}

	public int deleteBomDoc(DocMgrVO docVO) {
		return sqlSession.update("mappers.ProdDocMgrMapper.deleteBomDoc", docVO);
	}


	public int selectQryAttchFileCnt(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectQryAttchFileCnt", docVO);
	}


	public int selectQryBomAttchFileCnt(DocMgrVO docVO) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectQryBomAttchFileCnt", docVO);
	}


	public List<DocMgrVO> selectBomInfoList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectBomInfoList", vo);
	}


	public int selectBomInfoListCnt(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectBomInfoListCnt", vo);
	}


	public List<DocMgrVO> selectProdInfoList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectProdInfoList", vo);
	}


	public int selectProdInfoListCnt(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectProdInfoListCnt", vo);
	}


	public List<DocMgrVO> prodDocMasterMtrList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.prodDocMasterMtrList", vo);
	}


	public int selectCntRqrdDocsMtr(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectCntRqrdDocsMtr", vo);
	}


	public int insertRqrdDocsMtr(DocMgrVO vo) {
		return sqlSession.insert("mappers.ProdDocMgrMapper.insertRqrdDocsMtr", vo);
	}


	public int updateRqrdDocsMtr(DocMgrVO vo) {
		return sqlSession.update("mappers.ProdDocMgrMapper.updateRqrdDocsMtr", vo);
	}

	public List<DocMgrVO> selectProcDocMgrListSKU(DocMgrVO docVO) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectProcDocMgrListSKU", docVO);
	}
	
	public int selectProcDocMgrListSKUCnt(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectProcDocMgrListSKUCnt", vo);
	}


	public List<DocMgrVO> selectBomInfoListR(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectBomInfoListR", vo);
	}


	public int selectBomInfoListRCnt(DocMgrVO vo) {
		return sqlSession.selectOne("mappers.ProdDocMgrMapper.selectBomInfoListRCnt", vo);
	}


	public List<SheetMgrVO> selectBomInfoList2(DocMgrVO vo) {
		return sqlSession.selectList("mappers.ProdDocMgrMapper.selectBomInfoList2", vo);
	}

	

}