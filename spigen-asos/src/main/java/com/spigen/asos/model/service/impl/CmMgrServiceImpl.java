package com.spigen.asos.model.service.impl;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.CmMgrService;

@Service("cmMgrService")
public class CmMgrServiceImpl implements CmMgrService {
	
	@Resource(name = "cmMgrDAO")
	CmMgrDAO cmMgrDAO;

	@Override
	public List<CmMgrVO> selectGnrCdList(CmMgrVO vo) throws Exception {
		return cmMgrDAO.selectGnrCdList(vo);
	}

	@Override
	public List<DocMgrVO> selectGnrCdDetailList(CmMgrVO vo) throws Exception {
		return cmMgrDAO.selectGnrCdDetailList(vo);
	}

	@Override
	public int insertGnrCdRgt(CmMgrVO vo) throws Exception {
		return cmMgrDAO.insertGnrCdRgt(vo);
	}

	@Override
	public int insertGnrCdDetailRgt(CmMgrVO vo) throws Exception {
		return cmMgrDAO.insertGnrCdDetailRgt(vo);
	}

	@Override
	public CmMgrVO selectGnrCdQry(CmMgrVO vo) throws Exception {
		return cmMgrDAO.selectGnrCdQry(vo);
	}

	@Override
	public int updateGnrCdUpd(CmMgrVO vo) throws Exception {
		return cmMgrDAO.updateGnrCdUpd(vo);
	}

	@Override
	public CmMgrVO selectGnrCdDetailQry(CmMgrVO vo) throws Exception {
		return cmMgrDAO.selectGnrCdDetailQry(vo);
	}

	@Override
	public int updateGnrCdDetailUpd(CmMgrVO vo) throws Exception {
		return cmMgrDAO.updateGnrCdDetailUpd(vo);
	}

	@Override
	public List<CmMgrVO> selectProdInfoList(DocMgrVO vo) throws Exception {
		return cmMgrDAO.selectProdInfoList(vo);
	}

	@Override
	public List<DocMgrVO> selectVdrList(DocMgrVO vo) throws Exception {
		return cmMgrDAO.selectVdrList(vo);
	}

	@Override
	public List<DocMgrVO> vndrBySKUList(DocMgrVO vo) throws Exception {
		return cmMgrDAO.vndrBySKUList(vo);
	}

	@Override
	public int insertSKUByVndr(DocMgrVO vo) throws Exception {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		vo.setCrtrId(principal.getName());
		return cmMgrDAO.insertSKUByVndr(vo);
	}

	@Override
	public int deleteSKUByVndr(DocMgrVO vo) throws Exception {
		return cmMgrDAO.deleteSKUByVndr(vo);
	}

	@Override
	public List<FileMgrVO> selectClntList(FileMgrVO vo) throws Exception {
		return cmMgrDAO.selectClntList(vo);
	}
}
