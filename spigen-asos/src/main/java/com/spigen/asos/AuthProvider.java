package com.spigen.asos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spigen.asos.model.UserVO;
import com.spigen.asos.model.service.SecurityService;

@Component
public class AuthProvider implements AuthenticationProvider{
    
    @Autowired
    SecurityService securityService;
 
    //로그인 버튼을 누를 경우
 
    //첫번째 실행
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	System.out.println("dsds");
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authenticate(id, password);
    }
    
    //두번쨰 실행
    private Authentication authenticate(String id, String password) throws AuthenticationException{
        
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        
        UserVO member = new UserVO();
            
        member = (UserVO)securityService.loadUserByUsername(id);
    
        if ( member == null ){
            System.out.println("사용자 정보가 없습니다.");
            throw new UsernameNotFoundException(id);
        }else if(member != null && !member.getPassword().equals(password) ) {
        	System.out.println("비밀번호가 틀렸습니다.");
            throw new BadCredentialsException(id);
        }
    
        grantedAuthorityList.add(new SimpleGrantedAuthority(member.getUserRole()));
        grantedAuthorityList.add(new SimpleGrantedAuthority(member.getRoleInfo()));
        grantedAuthorityList.add(new SimpleGrantedAuthority(member.getLifnr()));
        
        return new MyAuthentication(id, password, grantedAuthorityList, member);
 
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
 
}

