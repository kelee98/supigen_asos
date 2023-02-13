package com.spigen.asos.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spigen.asos.model.aitem;
import com.spigen.asos.model.inList;
import com.spigen.asos.model.inListInterface;
import com.spigen.asos.model.stocks;

//JpaRepository 에서 기본적인 crud 제공, 매개변수로 도메인클래스와 키타입을 기술
public interface AitemRepository extends JpaRepository<aitem, String>{
}