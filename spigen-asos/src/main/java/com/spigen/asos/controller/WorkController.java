package com.spigen.asos.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.spigen.asos.model.Odrlist;
import com.spigen.asos.model.Vdrlist;
import com.spigen.asos.model.exableList;
import com.spigen.asos.model.service.DataService;
import com.spigen.asos.model.service.TradeDocMgrService;
import com.spigen.asos.repository.VdrListRepository;
import com.spigen.asos.model.service.exWatingListService;

@Controller
public class WorkController {
	
	
	@Autowired
    private DataService dataservice;
	
    @GetMapping(value = { "/orderWork" })
    public String orderWork(Model model) throws Exception{
    	List<Vdrlist> VendorLists = dataservice.VdrfindAll();
    	model.addAttribute("VendorLists", VendorLists);
        return "orderWork";
    }
    
    @GetMapping(value = { "/assignWork" })
    public String assignWork(Model model) throws Exception{
    	List<Vdrlist> VendorLists = dataservice.VdrfindAll();
    	model.addAttribute("VendorLists", VendorLists);
        return "assignWork";
    }
    
    @GetMapping(value = {"/startWork" })
    public String startWork(Model model) throws Exception{
    	List<Vdrlist> VendorLists = dataservice.VdrfindAll();
    	model.addAttribute("VendorLists", VendorLists);
        return "startWork";
    }
    
    @GetMapping(value = { "/completeWork" })
    public String completeWork(Model model) throws Exception{
    	List<Vdrlist> VendorLists = dataservice.VdrfindAll();
    	model.addAttribute("VendorLists", VendorLists);
        return "completeWork";
    }
    
    @GetMapping(value = {"/manageWork" })
    public String manageWork(Model model) throws Exception{
    	List<Vdrlist> VendorLists = dataservice.VdrfindAll();
    	model.addAttribute("VendorLists", VendorLists);
    	return "manageWork";
    }
    
    @GetMapping(value = { "/in_management" })
    public String in_management( Model model) {
      //  model.addAttribute("boardList", boardService.findBoardList(pageable));
	//  List<Data4> VendorLists = datarepository.findAll();
  //	model.addAttribute("boardList", VendorLists);  
	  List<Vdrlist> inLists = dataservice.VdrfindAll();
  	    model.addAttribute("inLists", inLists);  
	  return "in_management";
    }
    
    @GetMapping(value = { "/ex_waiting" })
    public String ex_waiting_list( Model model) throws Exception {
      //  model.addAttribute("boardList", boardService.findBoardList(pageable));
	//  List<Data4> VendorLists = datarepository.findAll();
  //	model.addAttribute("boardList", VendorLists);  
    	List<Vdrlist> inLists = dataservice.VdrfindAll();
  	    model.addAttribute("inLists", inLists); 
	  
	  return "ex_waiting";
    }
    @GetMapping("/ex_processing")
    public String ex_processing_list( Model model) {
    	List<Vdrlist> inLists = dataservice.VdrfindAll();
        model.addAttribute("boardList",inLists);
        return "ex_processing";
    }
    
}
