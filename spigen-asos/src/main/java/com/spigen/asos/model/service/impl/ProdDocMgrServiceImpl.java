package com.spigen.asos.model.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.SheetMgrVO;
import com.spigen.asos.model.service.ProdDocMgrService;

@Service("prodDocMgrService")
public class ProdDocMgrServiceImpl implements ProdDocMgrService {
	
	@Resource(name = "prodDocMgrDAO")
	ProdDocMgrDAO prodDocMgrDAO;

	@Override
	public List<DocMgrVO> selectHeaderList(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectHeaderList(docVO);
	}

	@Override
	public List<DocMgrVO> selectDetailList(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectDetailList(docVO);
	}

	@Override
	public int insertRqrdDocs(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.insertRqrdDocs(docVO);
	}
	
	@Override
	public int deleteRqrdDocs(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.deleteRqrdDocs(docVO);
	}

	@Override
	public List<DocMgrVO> selectSDocMgrList(DocMgrVO docVO) throws Exception {
		
		List<DocMgrVO> mainList = prodDocMgrDAO.selectSDocMgrList(docVO);
		
		DocMgrVO subQryVO = new DocMgrVO();
		
		List<DocMgrVO> resultList = new ArrayList<DocMgrVO>();
		
		for(int i = 0; i<mainList.size(); i++) {
			List<DocMgrVO> subList = new ArrayList<DocMgrVO>();
			subQryVO.setMvgr2(mainList.get(i).getMvgr2());
			subQryVO.setMvgr3(mainList.get(i).getMvgr3());
			subQryVO.setNormt(mainList.get(i).getNormt());
			subQryVO.setComnYn(mainList.get(i).getComnYn());
			subList = prodDocMgrDAO.selectSDocMgrSubList(subQryVO);
			
			mainList.get(i).setSubList(subList);
			resultList.add(mainList.get(i));
		}
		
		return mainList;
	}
	
	@Override
	public int selectSDocMgrListCnt(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectSDocMgrListCnt(docVO);
	}

	@Override
	public List<DocMgrVO> selectCmQryList(String sqlId, DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectCmQryList(sqlId, docVO);
	}
	
	@Override
	public List<DocMgrVO> selectCntSkuList(DocMgrVO docVO) throws Exception {
		
		return prodDocMgrDAO.selectCntSkuList(docVO);
	}
	
	public int insertSDocFile(DocMgrVO docVO) throws Exception {
		int result = prodDocMgrDAO.insertSDocFile(docVO);
		int updateResult = 0;

		if(result>0) {
			docVO.setFinYn("");
			if("S".equals(docVO.getDocType())) {
				docVO.setSReq("F");
			}else if("A".equals(docVO.getDocType())) {
				docVO.setAReq("F");
			}
			//시방서, 승인원 완료처리
			updateResult = prodDocMgrDAO.updateSDocFile(docVO);
			if(updateResult>0) {
				if("Y".equals(prodDocMgrDAO.selectFinYn(docVO))){ //문서 전체 준비 여부 체크
					docVO.setSReq("");
					docVO.setAReq("");
					docVO.setFinYn("Y");
					// 완료여부 Y
					updateResult = prodDocMgrDAO.updateSDocFile(docVO);
				}
			}

		}
			return updateResult;
	}

	@Override
	public int insertProdDocFileChgHis(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.insertProdDocFileChgHis(docVO);
	}
	
	@Override
	public FileMgrVO selectFilePath(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectFilePath(docVO);
	}

	@Override
	public int selectCntRqrdDocs(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectCntRqrdDocs(vo);
	}

	@Override
	public int updateRqrdDocs(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.updateRqrdDocs(vo);
	}

	@Override
	public int selectCntRqrdDocsSKU(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectCntRqrdDocsSKU(vo);
	}

	@Override
	public int insertRqrdDocsSKU(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.insertRqrdDocsSKU(vo);
	}

	@Override
	public int updateRqrdDocsSKU(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.updateRqrdDocsSKU(vo);
	}

	@Override
	public DocMgrVO selectMasterInfo(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectMasterInfo(vo);
	}
	
	@Override
	public DocMgrVO selectBomMasterInfo(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomMasterInfo(vo);
	}

	@Override
	public List<DocMgrVO> selectAttachedFileList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectAttachedFileList(vo);
	}

	@Override
	public int updateProdDocFile(DocMgrVO docVO) throws Exception {

		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		int result = deleteProdDoc(docVO);
		if(result>0) {
			result = insertSDocFile(docVO);
		}
		
		//내역 저장
		docVO.setChgCd("U");
		docVO.setCrtrId(principal.getName());
		prodDocMgrDAO.insertProdDocFileChgHis(docVO);
		
		return result;
	}

	@Override
	public int deleteProdDoc(DocMgrVO docVO) throws Exception {

		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		int result = 0;
		
		docVO.setPath("/prd/removed");
		docVO.setCrtrId(principal.getName());
		result = prodDocMgrDAO.deleteProdDoc(docVO);
		
		int remainFiles = prodDocMgrDAO.selectQryAttchFileCnt(docVO);
		
		if(remainFiles == 0) {
			if("S".equals(docVO.getDocType())) {
				docVO.setSReq("Y");
			}else if("A".equals(docVO.getDocType())) {
				docVO.setAReq("Y");
			}
		
			prodDocMgrDAO.updateSDocFile(docVO);
		
			docVO.setSReq("");
			docVO.setAReq("");
			docVO.setFinYn("N");
			// 완료여부 N
			prodDocMgrDAO.updateSDocFile(docVO);
		}
		
		//내역 저장
		docVO.setChgCd("D");
		docVO.setCrtrId(principal.getName());
		prodDocMgrDAO.insertProdDocFileChgHis(docVO);
		
		return result;
	}

	@Override
	public List<DocMgrVO> selectBomMgrList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomMgrList(vo);
	}

	@Override
	public int selectBomMgrListCnt(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomMgrListCnt(vo);
	}

	@Override
	public int insertBomDocFile(DocMgrVO docVO) throws Exception {
		int result = prodDocMgrDAO.insertBomDocFile(docVO);
		int updateResult = 0;
		if(result>0) {
			docVO.setReq("F");
			docVO.setFinYn("Y");
			updateResult = prodDocMgrDAO.updateBomDocFile(docVO);
			
			//내역 저장
			docVO.setChgCd("C");
			prodDocMgrDAO.insertBomDocFileChgHis(docVO);
		}
			return updateResult;		
	}

	@Override
	public List<DocMgrVO> selectAttachedBomFileList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectAttachedBomFileList(vo);
	}
	
	@Override
	public int updateBomDocFile(DocMgrVO docVO) throws Exception {
		int result = 0;
		System.out.println("docVO.getDelYn():" + docVO.getDelYn());
		if("Y".equals(docVO.getDelYn())) {
			result = deleteBomDoc(docVO);
		}
		
		result = insertBomDocFile(docVO);
		
		//내역 저장
//		docVO.setChgCd("U");
//		prodDocMgrDAO.insertBomDocFileChgHis(docVO);
		
		return result;
	}

	@Override
	public int deleteBomDoc(DocMgrVO docVO) throws Exception {

		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		
		int result = prodDocMgrDAO.deleteBomDoc(docVO);
		
		int remainFiles = prodDocMgrDAO.selectQryBomAttchFileCnt(docVO);
		
		if(remainFiles == 0) {
			docVO.setReq("Y");
			docVO.setFinYn("N");
			prodDocMgrDAO.updateBomDocFile(docVO);
		}

		//내역 저장
		docVO.setChgCd("D");
		prodDocMgrDAO.insertBomDocFileChgHis(docVO);
		
		return result;
	}

	@Override
	public List<DocMgrVO> selectBomInfoList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomInfoList(vo);
	}

	@Override
	public int selectBomInfoListCnt(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomInfoListCnt(vo);
	}

	@Override
	public List<DocMgrVO> selectProdInfoList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectProdInfoList(vo);
	}

	@Override
	public int selectProdInfoListCnt(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectProdInfoListCnt(vo);
	}

	@Override
	public List<DocMgrVO> prodDocMasterMtrList(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.prodDocMasterMtrList(vo);
	}

	@Override
	public int selectCntRqrdDocsMtr(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectCntRqrdDocsMtr(vo);
	}

	@Override
	public int insertRqrdDocsMtr(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.insertRqrdDocsMtr(vo);
	}

	@Override
	public int updateRqrdDocsMtr(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.updateRqrdDocsMtr(vo);
	}
	
	@Override
	public List<DocMgrVO> selectProcDocMgrListModel(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectSDocMgrList(docVO);
	}
	
	@Override
	public List<DocMgrVO> selectProcDocMgrListSKU(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectProcDocMgrListSKU(docVO);
	}
	
	@Override
	public int selectProcDocMgrListSKUCnt(DocMgrVO docVO) throws Exception {
		return prodDocMgrDAO.selectProcDocMgrListSKUCnt(docVO);
	}

	@Override
	public List<DocMgrVO> selectBomInfoListR(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomInfoListR(vo);
	}

	@Override
	public int selectBomInfoListRCnt(DocMgrVO vo) throws Exception {
		return prodDocMgrDAO.selectBomInfoListRCnt(vo);
	}


	@Override
	public List<SheetMgrVO> selectBomInfoList2(DocMgrVO vo) {
		return prodDocMgrDAO.selectBomInfoList2(vo);
	}
}
