package com.spigen.asos.model.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;

@Repository("cmMgrDAO")
public class CmMgrDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public List<CmMgrVO> selectGnrCdList(CmMgrVO vo) {
		return sqlSession.selectList("mappers.CmMgrMapper.selectGnrCdList", vo);
	}

	public List<DocMgrVO> selectGnrCdDetailList(CmMgrVO vo) {
		return sqlSession.selectList("mappers.CmMgrMapper.selectGnrCdDetailList", vo);
	}

	public int insertGnrCdRgt(CmMgrVO vo) {
		return sqlSession.insert("mappers.CmMgrMapper.insertGnrCdRgt", vo);
	}

	public int insertGnrCdDetailRgt(CmMgrVO vo) {
		return sqlSession.insert("mappers.CmMgrMapper.insertGnrCdDetailRgt", vo);
	}
	//
	public CmMgrVO selectGnrCdQry(CmMgrVO vo) {
		return sqlSession.selectOne("mappers.CmMgrMapper.selectGnrCdQry", vo);
	}
	public int updateGnrCdUpd(CmMgrVO vo) {
		return sqlSession.update("mappers.CmMgrMapper.updateGnrCdUpd", vo);
	}

	public CmMgrVO selectGnrCdDetailQry(CmMgrVO vo) {
		return sqlSession.selectOne("mappers.CmMgrMapper.selectGnrCdDetailQry", vo);
	}

	public int updateGnrCdDetailUpd(CmMgrVO vo) {
		return sqlSession.update("mappers.CmMgrMapper.updateGnrCdDetailUpd", vo);
	}

	public List<CmMgrVO> selectProdInfoList(DocMgrVO vo) {
		//1:제조사 2:기종 3:모델
		if("1".equals(vo.getQryFlag())){
			return sqlSession.selectList("mappers.CmMgrMapper.selectProdInfoList_001", vo);
		}else if("2".equals(vo.getQryFlag())) {
			return sqlSession.selectList("mappers.CmMgrMapper.selectProdInfoList_002", vo);
		}else {
			return sqlSession.selectList("mappers.CmMgrMapper.selectProdInfoList_003", vo);
		}
	}

	public List<DocMgrVO> selectVdrList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.CmMgrMapper.selectVdrList", vo);
	}

	public List<DocMgrVO> vndrBySKUList(DocMgrVO vo) {
		return sqlSession.selectList("mappers.CmMgrMapper.vndrBySKUList", vo);
	}

	public int insertSKUByVndr(DocMgrVO vo) {
		return sqlSession.insert("mappers.CmMgrMapper.insertSKUByVndr", vo);
	}

	public int deleteSKUByVndr(DocMgrVO vo) {
		// TODO Auto-generated method stub
		return sqlSession.delete("mappers.CmMgrMapper.deleteSKUByVndr", vo);
	}

	public List<FileMgrVO> selectClntList(FileMgrVO vo) {
		return sqlSession.selectList("mappers.CmMgrMapper.selectClntList", vo);
	}

}
