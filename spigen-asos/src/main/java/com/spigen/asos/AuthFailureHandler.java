package com.spigen.asos;
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.spigen.asos.model.JSONResult;
import com.spigen.asos.model.service.SecurityService;
 
/**
 * 로그인 실패 핸들러
 */
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler  {
    
    @Autowired
    SecurityService securityService;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthFailureHandler.class);
    
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        
        String id = "";
        String msg = "로그인에 실패하였습니다.";
 
        try {
            System.out.println("실패핸들러");
            id = exception.getMessage();
            
//        if(securityService.getSelectMeberInfo(id) != null) {
//            securityService.setUpdatePasswordLockCnt(id);
//            msg="비밀번호가 틀렸습니다.";
//        }else {
//            msg="아이디가 없습니다.";
//        }
        String accept = request.getHeader("accept");

        response.setCharacterEncoding("utf-8");

        // application/json(ajax) 요청일 경우 아래의 처리!
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        JSONResult jsonResult = JSONResult.success("msg", msg);
        if (jsonConverter.canWrite(jsonResult.getClass(), jsonMimeType)) {
            jsonConverter.write(jsonResult, jsonMimeType, new ServletServerHttpResponse(response));
        }    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        //response.sendRedirect("/login?msg="+msg);
    }
 
}


