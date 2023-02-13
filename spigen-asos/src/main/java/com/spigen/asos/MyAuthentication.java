package com.spigen.asos;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import com.spigen.asos.model.UserVO;

import lombok.Data;
 
 
//현재 로그인한 사용자 객체 저장 DTO
@Data
public class MyAuthentication extends UsernamePasswordAuthenticationToken{
    private static final long serialVersionUID = 1L;
    
    UserVO member;
    
    public MyAuthentication(String id, String password, List<GrantedAuthority> grantedAuthorityList, UserVO member) {
        super(id, password, grantedAuthorityList);
        System.out.println("=================================");
        System.out.println("userRole:" + member.getUserRole());
        System.out.println("userRole:" + member.getId());
        this.member = member;
    }
}


