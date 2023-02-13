package com.spigen.asos.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.spigen.asos.model.Vdrlist;

//JpaRepository 에서 기본적인 crud 제공, 매개변수로 도메인클래스와 키타입을 기술
public interface VdrListRepository extends JpaRepository<Vdrlist, Long>{

}