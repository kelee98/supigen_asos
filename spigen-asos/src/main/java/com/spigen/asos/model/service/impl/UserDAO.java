package com.spigen.asos.model.service.impl;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.UserVO;

@Repository("userDAO")
@Mapper
public class UserDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public UserVO selectUserInfo(String userId) {
		return sqlSession.selectOne("mappers.UserMapper.selectUserInfo", userId);
	}

	public String initPwProssYn(UserVO vo) {
		return sqlSession.selectOne("mappers.UserMapper.initPwProssYn", vo);
	}

	public int initPassword(UserVO vo) {
		return sqlSession.update("mappers.UserMapper.initPassword",vo);
	}

	public int createUser(UserVO user) {
		return sqlSession.insert("mappers.UserMapper.createUser", user);
	}

	public void createAuthority(UserVO user) {
		// TODO Auto-generated method stub
		
	}

	public int deleteUser(String username) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void deleteAuthority(String username) {
		// TODO Auto-generated method stub
		
	}

	public int idCheck(UserVO user) {
		return sqlSession.selectOne("mappers.UserMapper.idCheck", user);
	}

	public List<UserVO> selectUserMgrList(UserVO vo) {
		return sqlSession.selectList("mappers.UserMapper.selectUserMgrList", vo);
	}

	public int updInitPassword(UserVO vo) {
		return sqlSession.insert("mappers.UserMapper.updInitPassword", vo);
	}

	public void insertLoginHis(UserVO vo) {
		 sqlSession.insert("mappers.UserMapper.insertLoginHis", vo);
	}

	public int updUserInfo(UserVO vo) {
		 return sqlSession.insert("mappers.UserMapper.updUserInfo", vo);
		
	}
}
