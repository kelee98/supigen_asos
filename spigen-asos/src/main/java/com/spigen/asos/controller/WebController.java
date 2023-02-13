package com.spigen.asos.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spigen.asos.MyAuthentication;
import com.spigen.asos.model.FileMgrVO;
import com.spigen.asos.model.UserVO;
import com.spigen.asos.model.service.SecurityService;
import com.spigen.asos.model.service.impl.UserServiceImpl;

@Controller
public class WebController {
    
    //로그인 성공후 이동페이지
    @RequestMapping(value = { "/","/index" })
    public String home(Principal principal) {
    	
    	ModelMap model = new ModelMap();
    	
    	System.out.println(principal.getName());
    	AbstractAuthenticationToken auth = (AbstractAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
    	//UserDetails vo = (UserDetails) auth.getDetails();
    	System.out.println("obj:" + auth.getAuthorities().iterator().next().getAuthority());
        return  "index";
    }
    
	
    @RequestMapping("/topbar")
    public String top() {
    	return "topbar";
    }
    
    @RequestMapping("/nav")
    public String left() {
    	return "nav";
    }

}
