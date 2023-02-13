package com.spigen.asos.controller;

import java.security.Principal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.UserVO;
import com.spigen.asos.model.service.CmMgrService;
import com.spigen.asos.model.service.SecurityService;
import com.spigen.asos.model.service.impl.UserServiceImpl;

@Controller
public class UserMgrController {
	
	/** userService*/
	@Resource(name ="userService")
	private UserServiceImpl userService;
	 
    @Autowired
    SecurityService securityService;
    
    //회원가입 페이지 이동
    @GetMapping("/join/join")
    public String join() {
        return  "login/join";
    }
    
    //로그인 페이지 이동
    @RequestMapping("/login")
    public ModelAndView lgoin(@RequestParam(value="msg", required=false) String msg) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("msg",msg);
        mv.setViewName("login");
        System.out.println("msg:" + msg);
        return mv;
    }
    
    //로그아웃
    @RequestMapping("/logout")
    public String logout(HttpServletRequest req) {
    	System.out.println("왜여기로오니");
        return  "login";
    }
    
    //비밀번호 초기화 팝업 호출
    @RequestMapping("/login/passwordInit")
    public String passwordInit() {
        return "popup/passwordInit";
    }
    
	//초기화 가능여부 확인
	@RequestMapping("/login/initPwProssYn") @ResponseBody
	public ModelMap initPwProssYn(UserVO vo) throws Exception{
		ModelMap model = new ModelMap();
		
		String result = userService.initPwProssYn(vo);
		
		model.addAttribute("result", result);
		
		return model;
	}
	
    // 초기화
    @RequestMapping("/login/initPassword") @ResponseBody
    public ModelMap initPassword(UserVO vo) {
    	ModelMap model = new ModelMap();
        
        int result = userService.initPassword(vo);
        
        model.addAttribute("result", result);
        
        return model;
    }
    
    // 초기 비밀번호 등록
    @RequestMapping("/login/updInitPassword") @ResponseBody
    public ModelMap updInitPassword(UserVO vo) {
    	ModelMap model = new ModelMap();
    	
    	Principal principal = SecurityContextHolder.getContext().getAuthentication();
    	
    	System.out.println("name: " + principal.getName());
    	vo.setId(principal.getName());
    	vo.setUseYn("Y");
    	
    	int result = userService.updInitPassword(vo);
    	
    	model.addAttribute("result", result);
    	
    	return model;
    }
    
    //사용자 관리
    @RequestMapping("/userMgrList")
    public String userMgrListScreen() {
        return "userMgrList";
    }
    
    //사용자 등록
    @RequestMapping("/userMgrList/signUpPopup")
    public ModelAndView signUpPopupScreen() {
    	
    	ModelAndView model = new ModelAndView();
    	model.setViewName("popup/signUpPopup");
    	
    	return model;
    }
    
    // 등록
    @RequestMapping("/login/createUser") @ResponseBody
    public ModelMap createUser(UserVO vo) {
    	ModelMap model = new ModelMap();
    	
		 System.out.println("dddddddddddddddddddd");
		 vo.setAccountNonExpired(true);
		 vo.setAccountNonLocked(true);
		 vo.setCredentialsNonExpired(true);
		 vo.setEnabled(true);
		 vo.setEmail(vo.getId());
		 
    	int result = userService.createUser(vo);
    	
    	model.addAttribute("result", result);
    	
    	return model;
    }
    
    //아이디 중복체크
	@RequestMapping(value = "/userMgrList/signUpPopup/idCheck" , method = RequestMethod.POST) 
    @ResponseBody
    public ModelMap idCheck(UserVO vo) throws Exception{
    	System.out.println("========:" + vo.getId());
    	ModelMap model = new ModelMap();
        
        int result = userService.idCheck(vo);
        
        model.addAttribute("result", result);
        
     	return model;
    }
	
	//유저 목록 조회
	@RequestMapping(value = "/userMgrList/selectUserMgrList" , method = RequestMethod.POST) 
	@ResponseBody
	public ModelMap selectUserMgrList(UserVO vo) throws Exception{
		
		ModelMap model = new ModelMap();
		
		List<UserVO> resultList = userService.selectUserMgrList(vo);
		
		model.addAttribute("resultList", resultList);
		
		return model;
	}
	
    //사용자 정보 수정
    @RequestMapping("/userMgrList/updUserInfoPopup")
    public ModelAndView updUserInfoPopupScreen(UserVO vo) {
    	
    	ModelAndView model = new ModelAndView();

		List<UserVO> resultList = userService.selectUserMgrList(vo);

		model.addObject("resultList", resultList);
		model.setViewName("popup/updUserInfoPopup");

    	return model;
    }
    
    //사용자 정보 수정
    @RequestMapping("/userMgrList/updUserInfo")
	@ResponseBody
    public ModelMap updUserInfo(UserVO vo) {
    	
    	ModelMap model = new ModelMap();
    	
    	int result = userService.updUserInfo(vo);
    	
		model.addAttribute("result", result);
    	
    	return model;
    }
    
    //초기 비밀번호 변경 화면 
    @RequestMapping("/login/pwInitPopupScreen")
    public ModelAndView pwInitPopupScreen(UserVO vo) {
    	
    	ModelAndView model = new ModelAndView();
    	
    	model.setViewName("popup/passwordInit");
    	
    	return model;
    }
    
    //사용자 정보 수정
    @RequestMapping("/login/pwInitOpenYn")
	@ResponseBody
    public ModelMap pwInitOpenYn(UserVO vo) {
    	
    	ModelMap model = new ModelMap();
    	
    	Principal principal = SecurityContextHolder.getContext().getAuthentication();
    	
    	System.out.println("vo:" + vo.getId());
    	System.out.println("name1: " + principal.getName());

    	vo.setId(principal.getName());
    	vo.setUseYn("Y");
    	List<UserVO> returnVo = userService.selectUserMgrList(vo);
    	
    	String initYn = returnVo.get(0).getInitYn();
    	System.out.println("initYn: " + initYn);
    	if("Y".equals(initYn)) {
    		model.addAttribute("initYn", "Y");
    	}else {
    		model.addAttribute("initYn", "N");
    	}
    	
    	return model;
    }
    
    

}
