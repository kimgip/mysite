package com.poscodx.mysite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.ognl.enhance.LocalReference;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.vo.SiteVo;

public class SiteInterceptor implements HandlerInterceptor {
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private SiteService siteService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		SiteVo siteVo = (SiteVo)request.getServletContext().getAttribute("siteVo");
		
		if(siteVo == null) {
			request.getServletContext().setAttribute("siteVo", siteService.getSite());
		}
		
		// Locale
		System.out.println("resolver-locale: "+localeResolver.resolveLocale(request).getLanguage());
		request.setAttribute("language", localeResolver.resolveLocale(request).getLanguage());
		return true;
	}

}
