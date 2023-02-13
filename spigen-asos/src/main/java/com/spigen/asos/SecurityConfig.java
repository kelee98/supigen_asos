package com.spigen.asos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.spigen.asos.model.service.SecurityService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired 
    AuthProvider authProvider;
 
    @Autowired 
    AuthSuccessHandler authSuccessHandler;
    
    @Autowired 
    AuthFailureHandler authFailureHandler;
    
    @Autowired 
    SecurityService userService;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userService)
        .passwordEncoder(userService.passwordEncoder());    
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("this");
        //CROF 설정을 해제합니다
        //초기 개발시에만 설정합니다
        //http.csrf().disable();
        
        http.authorizeRequests() 
            
            // //누구나 접속할 수 있는 페이지이기 때문에 누구나 접근이 가능하다 (.permitAll())
            .antMatchers("/login"
                    , "/login/login-processing"
                    , "/login/login-error"
                    , "/login/passwordInit"
                    , "/join/join"
                    , "/join/idCheck"
                    , "/login/find"
                    , "/join/insert"
                    , "/logout"
                    , "/sDocMgrList2"
                    , "/cm/selectProdInfoList2"
                    , "/prodDocMgrList/selectProdDocMgrList2"
                    , "/file/fileViewer"
                    , "/prodDocMgrList/downloadProdDoc"
            ).permitAll()
            .antMatchers("/**").authenticated();  //기타 /** 의 경로는 인증을 필요로 한다
        http.headers().frameOptions().disable();
        http.formLogin() 
            .loginPage("/login")  //로그인 페이지는 /, /login (같은 페이지)두 페이지 에서 로그인을 실행할 것이다
            .loginProcessingUrl("/login/login-processing") //로그인 버튼을 누를시 /login-processing 경로로 
            .usernameParameter("id")  //input name 파라미터로 "id"를 받는다.
            .passwordParameter("password") //input name 파라미터로 "password"를 받는다.
            .successForwardUrl("/")
            .failureHandler(authFailureHandler) //로그인 실패시 수행하는 클래스
            .successHandler(authSuccessHandler) // 로그인 성공시 수행하는 클래스
            .permitAll()
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .deleteCookies("JSESSIONID")
	        .logoutSuccessUrl("/login")
	        .invalidateHttpSession(true);
        
    }
    
    //JSP의 리소스 파일이나 자바스크립트 파일이 저장된 경로는 무시를 한다
    @Override
    public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/vendor/**", "/assets/**");
    }
    
}

