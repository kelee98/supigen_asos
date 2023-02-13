package com.spigen.asos.model.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spigen.asos.model.UserVO;

public interface SecurityService extends UserDetailsService {

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException;
	
	public int idCheck(UserVO user);
	
    public int createUser(UserVO user);
    
    public int deleteUser(String username);

    public PasswordEncoder passwordEncoder();

	public void insertLoginHis(UserVO vo);
    
	
}
