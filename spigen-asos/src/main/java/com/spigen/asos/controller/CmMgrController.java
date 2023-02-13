package com.spigen.asos.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.service.CmMgrService;

@Controller
public class CmMgrController {
	
	/** cmMgrService*/
	@Resource(name ="cmMgrService")
	private CmMgrService cmMgrService;
	
	//일반코드 관리 화면 호출
	@RequestMapping(path = "/cm/gnrCdScreen", method = RequestMethod.GET)
	public ModelAndView gnrCdScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("gnrCdMgrList");
		model.addObject("liIndex", "nav7");
		
		return model;
	}
	
	//일반코드 관리 헤더 조회
	@RequestMapping(value = "/cm/selectGnrCdList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectGnrCdList(@RequestBody CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		
		List<CmMgrVO> resultList = cmMgrService.selectGnrCdList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//일반코드 관리 디테일 조회
	@RequestMapping(value = "/cm/selectGnrCdDetailList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectGnrCdDetailList(@RequestBody CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		
		List<DocMgrVO> resultList = cmMgrService.selectGnrCdDetailList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	
	//일반코드 등록 화면 호출
	@RequestMapping(path = "/cm/gnrCdRgtPopupScreen", method = RequestMethod.GET)
	public ModelAndView gnrCdRgtPopupScreen() throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("popup/gnrCdRgtPopup");
		model.addObject("liIndex", "nav7");
		
		return model;
	}
	
	
	//일반코드 등록
	@RequestMapping(value = "/cm/insertGnrCdRgt" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap insertGnrCdRgt(CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		int result = cmMgrService.insertGnrCdRgt(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//상세코드 등록 화면 호출
	@RequestMapping(path = "/cm/gnrCdDetailRgtPopupScreen", method = RequestMethod.GET)
	public ModelAndView gnrCdDetailRgtPopupScreen(CmMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		model.setViewName("popup/gnrCdDetailRgtPopup");
		model.addObject("gnrCd", vo.getGnrCd());
		
		return model;
	}
	
	//상세코드 등록
	@RequestMapping(value = "/cm/insertGnrCdDetailRgt" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap insertGnrCdDetailRgt(CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		int result = cmMgrService.insertGnrCdDetailRgt(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//일반코드 수정 화면 호출
	@RequestMapping(path = "/cm/gnrCdUpdPopupScreen", method = RequestMethod.GET)
	public ModelAndView gnrCdUpdPopupScreen(CmMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();

		CmMgrVO returnVO = new CmMgrVO();
		
		returnVO = cmMgrService.selectGnrCdQry(vo);

		model.setViewName("popup/gnrCdUpdPopup");
		model.addObject("resultVO", returnVO);
		
		return model;
	}
	
	//일반코드 수정
	@RequestMapping(value = "/cm/updateGnrCdUpd" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap updateGnrCdUpd(CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();

		int result = cmMgrService.updateGnrCdUpd(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//상세코드 수정 화면 호출
	@RequestMapping(path = "/cm/gnrCdDetailUpdPopupScreen", method = RequestMethod.GET)
	public ModelAndView gnrCdDetailUpdPopupScreen(CmMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
				
		CmMgrVO returnVO = new CmMgrVO();
		
		returnVO = cmMgrService.selectGnrCdDetailQry(vo);
		
		model.setViewName("popup/gnrCdDetailUpdPopup");
		model.addObject("resultVO", returnVO);
		
		return model;
	}
	
	//상세코드 수정
	@RequestMapping(value = "/cm/updateGnrCdDetailUpd" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap updateGnrCdDetailUpd(CmMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();

		int result = cmMgrService.updateGnrCdDetailUpd(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	
	//제조사,기종,모델명 조회
	@RequestMapping(value = "/cm/selectProdInfoList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectProdInfoList(@RequestBody DocMgrVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		List<CmMgrVO> resultList = cmMgrService.selectProdInfoList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	//제조사,기종,모델명 조회
	@RequestMapping(value = "/cm/selectProdInfoList2" , method = RequestMethod.GET) 
	@ResponseBody
	public ModelMap selectProdInfoList2(@RequestParam("mvgr2") String mvgr2, @RequestParam("mvgr3") String mvgr3, @RequestParam("qryFlag") String qryFlag) throws Exception{
		
		DocMgrVO vo = new DocMgrVO();
		vo.setMvgr2(mvgr2);
		vo.setMvgr3(mvgr3);
		vo.setQryFlag(qryFlag);
		ModelMap model = new ModelMap();
		
		List<CmMgrVO> resultList = cmMgrService.selectProdInfoList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}

	//상세코드 수정 화면 호출
	@RequestMapping(path = "/cm/searchHelper", method = RequestMethod.GET)
	public ModelAndView searchHelper(CmMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		
		model.setViewName("popup/searchHelper");
		
		return model;
	}
	
	//벤더별 상품 목록 조회 화면 호출
	@RequestMapping(path = "/cm/prdByVndrMgrListScreen", method = RequestMethod.GET)
	public ModelAndView prdByVndrMgrListScreen(DocMgrVO vo) throws Exception{
		ModelAndView model = new ModelAndView();
		
		model.setViewName("prdByVndrMgrList");
		
		return model;
	}
	
	//벤더별 목록 조회
	@RequestMapping(path = "/cm/prdByVndrMgrList", method = RequestMethod.POST) @ResponseBody
	public ModelMap prdByVndrMgrList(@RequestBody DocMgrVO vo) throws Exception{
		System.out.println("vo.getVdrNm():" + vo.getVdrNm());
		ModelMap model = new ModelMap();
		
		List<DocMgrVO> resultList = cmMgrService.selectVdrList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//벤더별 SKU 목록 조회
	@RequestMapping(path = "/cm/vndrBySKUList", method = RequestMethod.POST) @ResponseBody
	public ModelMap vndrBySKUList(@RequestBody DocMgrVO vo) throws Exception{
		System.out.println("vo.getLifnr():" + vo.getLifnr());
		ModelMap model = new ModelMap();
		
		List<DocMgrVO> resultList = cmMgrService.vndrBySKUList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
	//벤더별 SKU 등록
	@RequestMapping(path = "/cm/insertSKUByVndr", method = RequestMethod.POST) @ResponseBody
	public ModelMap insertSKUByVndr(@RequestBody DocMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		int result = cmMgrService.insertSKUByVndr(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
	//벤더별 SKU 삭제
	@RequestMapping(path = "/cm/deleteSKUByVndr", method = RequestMethod.POST) @ResponseBody
	public ModelMap deleteSKUByVndr(@RequestBody DocMgrVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		int result = cmMgrService.deleteSKUByVndr(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
}
