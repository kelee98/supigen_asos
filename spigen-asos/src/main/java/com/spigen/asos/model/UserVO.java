package com.spigen.asos.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	    
    private String id;
    private String password;
    private String email;
    private String userRole;
    private String roleId;
    private int passwordLock;
    private String regDate;
    private String modDate;
    private String passwordChgDate;
    private String status;
    private String roleInfo;
    private String useYn;
    private String initYn;
    private String userIp;
    private String lifnr;
    
    /*UserDetails 기본 상속 변수 */
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled = true;
    private String username;
    private boolean isCredentialsNonExpired = true;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
	
}
