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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.spigen.asos.model.JSONResult;
import com.spigen.asos.model.UserVO;
import com.spigen.asos.model.service.SecurityService;
 
/*
 * 스프링 시큐리티를 사용하며, 로그인 성공시 부가 작업을 하려면, 
 * org.springframework.security.web.authentication.AuthenticationSuccessHandler를 구현해야 한다.
 *별도로 authenticationSuccessHandler를 지정하지 않으면 기본적으로 
 * org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler를 사용하게 된다.
 */
 
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    
    private static final Logger logger = LoggerFactory.getLogger(AuthSuccessHandler.class);
    
    @Autowired
    SecurityService securityService;
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
      
        String id = "";
        try {
            System.out.println("성공 핸들러");

            id = authentication.getName().toString();                        
//            securityService.setUpdatePasswordLockCntReset(id);
            
            
            UserVO vo = new UserVO();
            vo.setId(id);
            vo.setUserIp(getRemoteAddr(request));
            securityService.insertLoginHis(vo);
            
            response.setCharacterEncoding("utf-8");

            // application/json(ajax) 요청일 경우 아래의 처리!
            MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
            MediaType jsonMimeType = MediaType.APPLICATION_JSON;

            JSONResult jsonResult = JSONResult.success("msg", "S");
            jsonResult.setResult(authentication.getAuthorities().iterator().next().getAuthority());
            if (jsonConverter.canWrite(jsonResult.getClass(), jsonMimeType)) {
                jsonConverter.write(jsonResult, jsonMimeType, new ServletServerHttpResponse(response));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        super.setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
 
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        return ip;
    }
}

