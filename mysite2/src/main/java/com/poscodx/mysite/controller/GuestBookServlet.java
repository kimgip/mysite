package com.poscodx.mysite.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.dao.GuestbookDao;
import com.poscodx.mysite.vo.GuestbookVo;

public class GuestBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
		if("insert".equals(action)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(content);
			
			new GuestbookDao().insert(vo);
			
			response.sendRedirect(request.getContextPath()+"/guestbook");
		}
		else if("deleteform".equals(action)) {
			request
				.getRequestDispatcher("WEB-INF/views/guestbook/deleteform.jsp")
				.forward(request, response);
		} else if("delete".equals(action)) {
			Long no = Long.parseLong(request.getParameter("no"));
			String password = request.getParameter("password");
			new GuestbookDao().deleteByNoAndPassword(no, password);
			
			response.sendRedirect(request.getContextPath()+ "/guestbook");
		} else {
			request.setAttribute("list", new GuestbookDao().findAll());
			request
				.getRequestDispatcher("WEB-INF/views/guestbook/list.jsp")
				.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
