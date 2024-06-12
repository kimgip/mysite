package com.poscodx.mysite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.service.SiteService;
import com.poscodx.mysite.vo.SiteVo;

public class SiteInterceptor implements HandlerInterceptor {
	private SiteService siteService;
	
	public SiteInterceptor(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("SiteInterceptor preHandle");
		SiteVo siteVo = (SiteVo)request.getServletContext().getAttribute("siteVo");
		
		if(siteVo == null) {
			request.getServletContext().setAttribute("siteVo", siteService.getSite());
		}
		return true;
	}

}