package com.spigen.asos.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spigen.asos.model.CmMgrVO;
import com.spigen.asos.model.DocMgrVO;
import com.spigen.asos.model.UserVO;
import com.spigen.asos.model.service.CmMgrService;
import com.spigen.asos.model.service.SecurityService;

@Service("userService")
public class UserServiceImpl implements SecurityService {
	
	@Resource(name = "userDAO")
	UserDAO userDAO;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 UserInfo로 반환 타입 지정 (자동으로 다운 캐스팅됨)
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
		UserVO userVO = userDAO.selectUserInfo(userId);	
		System.out.println("[[[[[[[[[[[[[[" + userVO.getId());
		System.out.println("[[[[[[[[[[[[[[" + userVO.getUsername());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
	   if(userVO != null) {
             authorities.add(new SimpleGrantedAuthority(userVO.getUserRole()));
             userVO.setAuthorities(authorities);
         }
         return userVO;
	}

	public String initPwProssYn(UserVO vo) {
		return userDAO.initPwProssYn(vo);
	}

	public int initPassword(UserVO vo) {
		return userDAO.initPassword(vo);
	}

	@Override
	public int createUser(UserVO user) {
		int returnVal = 0;
		System.out.println("user:" + user.getId());
        String rawPassword = user.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);
        returnVal = userDAO.createUser(user);
        return returnVal;
	}

	@Override
	public int deleteUser(String username) {
		int returnVal = 0;
		returnVal = userDAO.deleteUser(username);
		userDAO.deleteAuthority(username);
		return returnVal;
	}

	@Override
	public PasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return this.passwordEncoder;
	}

	@Override
	public int idCheck(UserVO vo) {
		return userDAO.idCheck(vo);	
	}

	public List<UserVO> selectUserMgrList(UserVO vo) {
		return userDAO.selectUserMgrList(vo);	
	}

	public int updInitPassword(UserVO vo) {
		
        String rawPassword = vo.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        vo.setPassword(encodedPassword);
        
		return userDAO.updInitPassword(vo);
	}

	@Override
	public void insertLoginHis(UserVO vo) {
		userDAO.insertLoginHis(vo);		
	}

	public int updUserInfo(UserVO vo) {
		
        String rawPassword = "Spigen1234!";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        vo.setPassword(encodedPassword);
        
		return userDAO.updUserInfo(vo);		
	}

	
}
