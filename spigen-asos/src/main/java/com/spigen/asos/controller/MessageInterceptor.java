package com.spigen.asos.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class MessageInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		String lang = uri.substring(1, 3);
		
		HttpSession session = request.getSession();

		if(lang.equals("ko")) {
			Locale locale = new Locale("ko");
			session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		}else if(lang.equals("en")) {
			Locale locale = new Locale("en");
			session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		}
		return true;
	}

	
}
