package com.spigen.asos.model.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;

@Repository("tradeDocMgrDAO")
public class TradeDocMgrDAO  {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
 
	public List<DocMgrVO> selectBankInfoList(FileMgrVO fileVO) {
		return sqlSession.selectList("mappers.TradeDocMgrMapper.selectBankInfoList", fileVO);
	}

	public int insertBankInfo(FileMgrVO fileVO) {
		return sqlSession.insert("mappers.TradeDocMgrMapper.insertBankInfo", fileVO);
	}

	public int updateBankInfo(FileMgrVO fileVO) {
		return sqlSession.update("mappers.TradeDocMgrMapper.updateBankInfo", fileVO);
	}
	
	public int insertBankInfoHis(FileMgrVO fileVO) {
		return sqlSession.update("mappers.TradeDocMgrMapper.insertBankInfoHis", fileVO);
	}

	//전송 이력 저장
	public int insertSendHis(FileMgrVO fileVO) {
		return sqlSession.insert("mappers.TradeDocMgrMapper.insertSendHis", fileVO);
	}

	public List<FileMgrVO> selectfaxSendHisQry(FileMgrVO fileVO) {
		return sqlSession.selectList("mappers.TradeDocMgrMapper.selectfaxSendHisQry", fileVO);
	}

	public List<FileMgrVO> selectFileMgrListQry(FileMgrVO fileVO) {
		  if("http://127.0.0.1:8080".equals(fileVO.getReqURL()) || "http://localhost:8080".equals(fileVO.getReqURL())) {
			  return sqlSession.selectList("mappers.TradeDocMgrMapper.selectFileMgrListQry_local", fileVO);
		  }else {
			  return sqlSession.selectList("mappers.TradeDocMgrMapper.selectFileMgrListQry", fileVO);
		  }
	}

	
	public List<FileMgrVO> selectTaxFileMgrListQry(FileMgrVO fileVO) throws UnknownHostException {

		// Setting the destination file
		  if("http://127.0.0.1:8080".equals(fileVO.getReqURL()) || "http://localhost:8080".equals(fileVO.getReqURL())) {
			  return sqlSession.selectList("mappers.TradeDocMgrMapper.selectTaxFileMgrListQry_local", fileVO);
		  }else {
			  return sqlSession.selectList("mappers.TradeDocMgrMapper.selectTaxFileMgrListQry", fileVO);
		  }
	}

	public int updateSendYn(FileMgrVO fileVO) {
		return sqlSession.update("mappers.TradeDocMgrMapper.updateSendYn", fileVO);
	}
	
	public int updateSendYn2(FileMgrVO fileVO) {
		return sqlSession.update("mappers.TradeDocMgrMapper.updateSendYn2", fileVO);
	}
	
	public int selectSendYn(FileMgrVO fileVO) throws UnknownHostException {
		// Setting the destination file
		return sqlSession.selectOne("mappers.TradeDocMgrMapper.selectSendYn", fileVO);
	}

}