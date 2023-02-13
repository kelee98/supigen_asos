package com.spigen.asos.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spigen.asos.model.Data;
import com.spigen.asos.model.Data2;
import com.spigen.asos.model.Data3;
import com.spigen.asos.model.Data4;
import com.spigen.asos.model.Data5;
import com.spigen.asos.model.Odrlist;
import com.spigen.asos.model.Prdlist;
import com.spigen.asos.model.aitem;
import com.spigen.asos.model.odrjInterface;
import com.spigen.asos.model.service.DataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class ApiController {
	@Autowired
	private DataService dataService;
	//private DataRepository dataService;
	
    @GetMapping("/api/v1/get/VendorList")
    public List<Data> getListData() {
        List<Data> dataList = new ArrayList<>();
        
        for(int i=0; i < 10; i++) {
            Data data = new Data();
            data.setTitle("Title " + i);
            data.setDescription("Description " + i);
            data.setValue(i);
            
            dataList.add(data);
        }
        
        return dataList;
    }
    
    @GetMapping("/api/v1/get/data2")
    public List<Data2> getListData2() {
        List<Data2> dataList = new ArrayList<>();
        
        for(int i=0; i < 10; i++) {
            Data2 data = new Data2();
            data.setGroup("M00" + i+1);
            data.setSku("SGP" + i);
            data.setValue1("O");
            data.setValue2("X");
            data.setValue3("X");
            data.setValue4("O");
            data.setValue5("X");
            
            dataList.add(data);
        }
        
        return dataList;
    }
    
    @GetMapping("/api/v1/get/data3")
    public List<Data3> getListData3() {
        List<Data3> dataList = new ArrayList<>();
        
        for(int i=0; i < 10; i++) {
            Data3 data = new Data3();

            data.setName("심재원"+i);
            data.setAge(i+20);
            dataList.add(data);
        }
        return dataList;
    }


    // 모든 사원 조회
    @GetMapping("/api/v1/get/data4")
    public List<Data4> getListData4() {
    	List<Data4> data = dataService.findAll();
    	return data;
    }
    //페이징을 위한 조회
    @GetMapping("/api/v1/get/data5")
    public List<Data5> getListData5(Pageable pageable) {
    	List<Data5> data = dataService.findPageAll(pageable);

    	return data;
    }
    
    @PostMapping("/api/v1/get/OdrList")
    public List<odrjInterface> getOdrList(@RequestParam("value") String keyword) {
    	//List<Odrlist> data = dataService.findByVdrCode(keyword);
    	//System.out.println("keyword="+ keyword);
    	List<odrjInterface> data = dataService.odrjlist(keyword);
    	
    	return data;
    }
    
    @PostMapping("/api/v1/get/regisOdr")
    //@ResponseBody //자바객체를 http 응답 몸체로 전송
    public List<Odrlist> regisOdr(Odrlist odrlist) {//http 요청의 body 내용을 자바 객체로 전달받음
    	//System.out.println("===odrlist: " + odrlist.toString());
    	//dataService.createOdr(odrlist);
    	List<Odrlist> data = dataService.findByVdrCode(odrlist.getCornum());
    	//System.out.println("이거임" + data);
    	return data;
    }
    
    
    
    @PostMapping("/api/v1/get/registerOdr")
    @ResponseBody
    public ModelMap registerOdr(@RequestBody String data) {//http 요청의 body 내용을 자바 객체로 전달받음
    	System.out.println("3333333333333");
    	System.out.println("3333333333333" + data);
    	ModelMap model = new ModelMap();
	  try {
		  //직렬화 시켜 가져온 오브젝트 배열을 JSONArray 형식으로 바꿔준다.
		    JSONArray array = JSONArray.fromObject(data);
		        
		    //List<Map<String, Object>> resendList  = new ArrayList<Map<String, Object>>();

		 // Gson gson = new Gson();
		  System.out.println("22");
		  //ArrayList<Map<String, String>> info = gson.fromJson(String.valueOf(data),  new TypeToken<ArrayList<Map<String, String>>>() {}.getType());
		  System.out.println("24");

		  System.out.println("dd:" + array);

//	      List<Map<String, String>> info = new Gson().fromJson(String.valueOf(data),new TypeToken<List<Map<String, String>>>(){}.getType());
//	      System.out.println("info: " + info.get(0).get("assycd"));
	     
    	  JSONObject odr = (JSONObject)array.get(0);

    	  Odrlist ol = new Odrlist();
    	  String wkonum = "202000000000";
    	  ol.setWkonum(wkonum);
    	  
    	  dataService.workOrder(ol);

		  
		  for (int i = 0; i<array.size(); i++) {
	    	  aitem aitem = new aitem();
	    	  
	    	  JSONObject obj = (JSONObject)array.get(i);

	    	  System.out.println(obj.get("assycd"));
	    	  aitem.setWkonum("202000000000");
	    	  aitem.setAssycd(obj.get("assycd").toString());
	    	  aitem.setCreusr(obj.get("creusr").toString());
	    	  aitem.setCredat(obj.get("credat").toString());
	    	  aitem.setSddate(obj.get("sddate").toString());
	    	  aitem.setRemark(obj.get("remark").toString());
	    	  System.out.println("dfdfdf");
	    	  dataService.createOdr(aitem);

	      }  
	      model.put("result", true);
	  } catch (Exception e) {
		  model.put("result", false);
	  }
    	  
//    	System.out.println("odrlist: " + data);
//    	dataService.createOdr(odrlist);
//    	List<Odrlist> data = dataService.findByVdrCode(odrlist.getCornum());
//    	//System.out.println("이거임" + data);
    	return model;
    }
    
    @PostMapping("/api/v1/get/deleteOdr")
    //@ResponseBody //자바객체를 http 응답 몸체로 전송
    public List<Odrlist> deleteOdr(HttpServletRequest request) {//http 요청의 body 내용을 자바 객체로 전달받음
    	String[] ajaxMsg = request.getParameterValues("select");
    	String hiMsg = request.getParameter("hi");
    	System.out.println(hiMsg);
    	String to = null;
    	int size = ajaxMsg.length;
	    for(int i=0; i<size; i++) {
	        to = ajaxMsg[i];
	       dataService.deleteByWkonum(to);
	    }
	    List<Odrlist> data = dataService.findByVdrCode(hiMsg);
	    System.out.println(hiMsg);
	    return data;
    }
}
