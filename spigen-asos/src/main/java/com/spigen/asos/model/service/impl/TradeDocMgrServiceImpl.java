package com.spigen.asos.model.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.service.TradeDocMgrService;

@Service("tradeDocMgrService")
public class TradeDocMgrServiceImpl implements TradeDocMgrService {
	
	@Resource(name = "tradeDocMgrDAO")
	TradeDocMgrDAO tradeDocMgrDAO;

	//은행 정보 조회
	@Override
	public List<DocMgrVO> selectBankInfoList(FileMgrVO fileVO) throws Exception {
		return tradeDocMgrDAO.selectBankInfoList(fileVO);
	}

	//은행 정보 등록
	@Override
	public int insertBankInfo(FileMgrVO fileVO) throws Exception {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		fileVO.setRgtId(principal.getName());
		int result = tradeDocMgrDAO.insertBankInfo(fileVO);
		
		//내역저장
		if(result>0) {
			fileVO.setChgCd("C");
			tradeDocMgrDAO.insertBankInfoHis(fileVO);
		}
		return result;
	}
	
	//은행 정보 수정
	@Override
	public int updateBankInfo(FileMgrVO fileVO) throws Exception {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		fileVO.setRgtId(principal.getName());
		
		int result = tradeDocMgrDAO.updateBankInfo(fileVO);

		//내역저장
		if(result>0) {
			fileVO.setChgCd("U");
			tradeDocMgrDAO.insertBankInfoHis(fileVO);
		}
		
		return result;
	}

	//전송 이력 저장 및 전송여부 업데이트
	@Override
	public int insertSendHis(FileMgrVO fileVO) throws Exception {
		
		int result = tradeDocMgrDAO.insertSendHis(fileVO);
		Principal principal = SecurityContextHolder.getContext().getAuthentication();

		FileMgrVO updateVO = new FileMgrVO();
		String[] fileSeq = fileVO.getFileSeq().split(",");
		String[] srchCon = fileVO.getSrchCon().split(","); 
		String[] sendYn = fileVO.getSendYn().split(","); 
		for (int i = 0; i < fileSeq.length; i++) {
			String[] tmp = srchCon[i].split(":");
			String[] tmp2 = fileSeq[i].split(":");

			updateVO.setLaufd(tmp[0]);
			updateVO.setLaufi(tmp[1]);
			updateVO.setVblnr(tmp[2]);
			updateVO.setZbukr(tmp[3]);
			updateVO.setEviSeq(tmp2[0]);
			updateVO.setFileSeq(tmp2[1]);
			updateVO.setUpdId(principal.getName());
			updateVO.setReqURL(fileVO.getReqURL());
			
			if("Y".equals(sendYn[i])) {
				updateVO.setSendYn("R");
			}else {
				updateVO.setSendYn("Y");
			}
			
			if(tradeDocMgrDAO.selectSendYn(updateVO)>0) {
				tradeDocMgrDAO.updateSendYn2(updateVO); //존재-수정
			}else { 
				tradeDocMgrDAO.updateSendYn(updateVO); //미존재-등록
			}
		}
		
		return result;
	}

	@Override
	public List<FileMgrVO> selectfaxSendHisQry(FileMgrVO fileVO) throws Exception {
		return tradeDocMgrDAO.selectfaxSendHisQry(fileVO);
	}

	@Override
	public List<FileMgrVO> selectFileMgrListQry(FileMgrVO fileVO) throws Exception {
		return tradeDocMgrDAO.selectFileMgrListQry(fileVO);
	}
	
	@Override
	public List<FileMgrVO> selectTaxFileMgrListQry(FileMgrVO fileVO) throws Exception {
		String[] arr = fileVO.getFileType().split(",");
		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			ls.add(arr[i]);
		}
		fileVO.setFileTypeArr(ls);
		
		return tradeDocMgrDAO.selectTaxFileMgrListQry(fileVO);
	}


}
