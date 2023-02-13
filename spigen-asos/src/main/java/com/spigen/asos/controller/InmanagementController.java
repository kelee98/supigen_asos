package com.spigen.asos.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spigen.asos.model.Vdrlist;
import com.spigen.asos.model.inListInterface;
import com.spigen.asos.repository.InListRepository;
import com.spigen.asos.repository.VdrListRepository;

@RestController
public class InmanagementController {
	@Autowired
	 private InListRepository inlistrepository;
	@Autowired
	private VdrListRepository vdrListRepository;
	
	@GetMapping("/in_management/data")
	 public List<inListInterface> in_management_list(HttpServletRequest request) {
	    	String keyword = request.getParameter("value"); 
	    	String code = request.getParameter("code"); 
	 	System.out.println(keyword);
	 	System.out.println(code);
	 
		if(keyword.equals(" ")||keyword.equals("")) {
			//검색 조건 이 모두 null 값인 경우 전체 조회
			if(code.equals(" ")||code.equals("")) {
				List<inListInterface> data = inlistrepository.alljoininlist();
	    		System.out.println();
	    		return data;
			}
			//완제품 코드 만 검색 할 경우
			else {
				//SKU 복수 조회
			    List<String> matnrLs = new ArrayList<String>();
			     
			    String[] matnrArr = code.split(",");
			    
			    for(int i=0; i< matnrArr.length; i++){
			    	matnrLs.add(matnrArr[i].toString());
			    }
				List<inListInterface> data;
				System.out.println("sizs: " +matnrArr.length );
				if(matnrArr.length==0) {
					System.out.println("all");
					data = inlistrepository.findassninlistAll(matnrArr);
				}else {
					System.out.println("in");
					data = inlistrepository.findassninlist(matnrArr);
				}

				return data;
	    		}
			}
		//완제품 업체면 모두 검색 할 경우
	    else {
	    		List<inListInterface> data = inlistrepository.joininlist(keyword,code);
	    		System.out.println();
	    		return data;
	    	}
	    }
	 
}
