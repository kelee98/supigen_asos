package com.spigen.asos.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spigen.asos.model.exList;
import com.spigen.asos.model.stbot;
import com.spigen.asos.model.service.exWatingListService;

@RestController
public class exProcessingController {
	@Resource(name ="exWatingListService")
	private exWatingListService exWatingListService;
	//출고처리 목록
	  @GetMapping("/ex_processing/data")
	    public List<exList> processing_list() throws Exception {
	    	 List<exList> list=exWatingListService.selectEXList();
	    	 List<exList> Asnlist = new ArrayList<>();
			
	    	 int count=0;
	    	 String temp="";
			for(int i=0;i<list.size();i++) {
				
				if( !((list.get(i).getAsndky()).equals(temp))) {
					Asnlist.add(list.get(i));
					count++;
				}
			
				temp=list.get(i).getAsndky();
				
			}
			
			System.out.println("끝"+count);

			    return Asnlist;            	
	    }
	  //출고처리 리스트 (asn item 번호 다름)
	    @GetMapping("/ex_processing/detailList")
	     public List<exList> exprocessing_detail(@RequestParam(value="value",defaultValue="None") String asn) throws Exception {
	     	List<exList> list=exWatingListService.selectEXList();
	    	 List<exList> Asnlist = new ArrayList<>();
	 		
	 		
	 		for(int i=0; i<list.size() ;i++) {
	 			
	 			if( ((list.get(i).getAsndky()).equals(asn))) {
	 				Asnlist.add(list.get(i));
	 			}
	 		}
	 		
	 		

	 		    return Asnlist;          	
	     }
}
