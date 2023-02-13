package com.spigen.asos.model.service.impl;


import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.Data4;
import com.spigen.asos.model.Data5;
import com.spigen.asos.model.Odrlist;
import com.spigen.asos.model.Vdrlist;
import com.spigen.asos.model.aitem;
import com.spigen.asos.model.join;
import com.spigen.asos.model.odrjInterface;
import com.spigen.asos.model.service.DataService;
import com.spigen.asos.repository.AitemRepository;
import com.spigen.asos.repository.DataRepository;
import com.spigen.asos.repository.OdrListRepository;
import com.spigen.asos.repository.PageRepository;
import com.spigen.asos.repository.VdrListRepository;

@Service
public class DataServiceImpl implements DataService{
	@Autowired
	private DataRepository dataRepository;
	@Autowired
	private PageRepository pageRepository;
	@Autowired
	private VdrListRepository vdrListRepository;
	@Autowired
	private OdrListRepository odrListRepository;
	@Autowired
	private AitemRepository aitemRepository;

	
    @Override
    public List<Data4> findAll() {

    	return dataRepository.findAll();
    }
    
    @Override
    public List<Data5> findPageAll(Pageable pageable) {
    	return pageRepository.findAll();
    }
    
    @Override
    public List<Vdrlist> VdrfindAll() {
    	return vdrListRepository.findAll();
    }
    @Override
    public List<Odrlist> OdrfindAll() {
    	return odrListRepository.findAll();
    }
    @Override
    public List<Odrlist> findByVdrCode(String keyword) {
    	return odrListRepository.findByCornum(keyword);
    }
    //작업지시(수기) 등록
    @Override
    public void createOdr(aitem aitem){
    	System.out.println("dddddddddddddddddddd");
    	System.out.println("data: " + aitem.getAssycd());
    	aitemRepository.save(aitem);
    }
    
    @Override
	public 	void deleteByWkonum(String to){
    	odrListRepository.deleteByWkonum(to); //나는 심재원이다. 천재다 아무도 나를 따라올 수 없다. 
    }
    
    @Override
	public List<odrjInterface> odrjlist(String keyword){
		return odrListRepository.odrjlist(keyword);
    }

	@Override
	public void workOrder(Odrlist ol) {
		odrListRepository.save(ol);
	}
}
