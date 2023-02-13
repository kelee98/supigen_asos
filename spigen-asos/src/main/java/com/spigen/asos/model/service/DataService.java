package com.spigen.asos.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.spigen.asos.model.Data4;
import com.spigen.asos.model.Data5;
import com.spigen.asos.model.Odrlist;
import com.spigen.asos.model.Vdrlist;
import com.spigen.asos.model.aitem;
import com.spigen.asos.model.join;
import com.spigen.asos.model.odrjInterface;

public interface DataService{
	List<Data4> findAll();
	List<Data5> findPageAll(Pageable pageable);
	List<Vdrlist> VdrfindAll();
	List<Odrlist> OdrfindAll();
	List<Odrlist> findByVdrCode(String keyword);
	void createOdr(aitem aitem);
	void deleteByWkonum(String to);
	public List<odrjInterface> odrjlist(String keyword);
	void workOrder(Odrlist ol);

}