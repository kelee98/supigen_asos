package com.spigen.asos.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spigen.asos.model.exList;
import com.spigen.asos.model.exableList;
import com.spigen.asos.model.inListInterface;
import com.spigen.asos.model.stbot;
import com.spigen.asos.model.service.exWatingListService;
import com.spigen.asos.repository.exListRepository;
@RestController
public class exWaitingController {
	@Resource(name ="exWatingListService")
	private exWatingListService exWatingListService;
	@Autowired
	 private exListRepository exlistrepository;
	
	//출고대기 가능 리스트
    @GetMapping("/ex_waiting/data")
    public List<exableList> waitingable_list() throws Exception {
 	
    	List<exableList> data = exWatingListService.selectEXableList();
    	
    		return data; 	
    }
    //출고대기 목록
    @GetMapping("/ex_waiting/waitinglist/data")
    public List<exList> waiting_list() throws Exception {
 	
    
    	 List<exList> data=exWatingListService.selectEXList();
    	System.out.println("출고 대기 목록");
    		return data; 	
    }
    
    // 출고처리(출고시간 출고 날짜 작성)
    /* @PostMapping("/ex_waiting/modify")
 		public List<exList> ex_waiting_time(HttpServletRequest request,Data4 data4,exList ex) throws Exception{ 
 			logger.info("modify");
 	
 		/*체크박스에 체크된 값들의 no (key 값)를 문자열로 가져와서 길이 만큼 for문을 돌려 int로 parsing 한다 
 		 *  no 값을 delete 함수에 넣어 주면 끝 */
 	/*		long to = 0 ;   // Check value storage variable
 			long asn= 0;
 			
 			String[] ajaxMsg = request.getParameterValues("_selected_"); //Get checked values ​​in checkbox
 			String company_code = request.getParameter("title"); 
 			System.out.println(company_code);
 			String company_name=request.getParameter("content"); 
 			System.out.println(company_name);
 			String ex_date = request.getParameter("createdate"); 
 			System.out.println(ex_date);
 			data4.setShippingDate(ex_date);
 			String ex_time = request.getParameter("updatedate"); 
 			System.out.println(ex_time);
 			data4.setShippingTime(ex_time);
 			
 			
 			
 			
 			    int size = ajaxMsg.length;
 			  
 			    //Asn 번호 생성
 			    for(int i=0; i<size; i++) {
 			    	to = Long.parseLong(ajaxMsg[i]);
 			    	asn+=boardService.findByIdx(to).getIdx();
 			    }
 			    asn*=1234;
 			    
 			    //출고대기 리스트에 저장후 삭제 출고 리스트에 저장
 			    for(int i=1; i<size+1; i++) {
 			        to = Long.parseLong(ajaxMsg[i-1]);
 			       boardService.updateBoard(to, data4);
 			     
 			       System.out.println(boardService.findByIdx(to).getIdx());
 			       ex.setIdx(boardService.findByIdx(to).getIdx());
 			       ex.setCompanyname(boardService.findByIdx(to).getCompanyname());
 			       ex.setFinishpname(boardService.findByIdx(to).getFinishpname());
 			       ex.setShippingDate(boardService.findByIdx(to).getShippingDate());
 			       ex.setShippingTime(boardService.findByIdx(to).getShippingTime());
 			       ex.setAsn(asn);
 			       ex.setAsnitem(asn+i);
 			     
 			       boardService.updateExList(ex);
 			     boardService.deleteByIdx(to);
 			  
 			    }
 		
 			 List<exList> dataList=exlistrepository.findAll();
 			 System.out.println("완료");
 			    return dataList;                                  // delete 후 원래 페이지로
 	

 		}*/
}
