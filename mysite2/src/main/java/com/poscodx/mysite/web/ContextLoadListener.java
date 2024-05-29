package com.poscodx.mysite.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoadListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
    	ServletContext sc = sce.getServletContext(); // Event를 일으킨 당사자 불러오기
    	String contextConfigLocation = sc.getInitParameter("contextConfigLocation");
    	
    	System.out.println("Application[Mysite2] starts..." + contextConfigLocation);
    }
	
    public void contextDestroyed(ServletContextEvent sce)  { 
    }
}
