package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;

public class ViewFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("p") == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		String p = request.getParameter("p");
		
		if (request.getParameter("no") == null) {
			response.sendRedirect(request.getContextPath() + "/board?p=" + p);
			return;
		}
		
		Long no = Long.parseLong(request.getParameter("no"));
		
		boolean visited = false;
		String views = "";
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if("viewsList".equals(cookie.getName())) {
					views = cookie.getValue();
					for (String v : views.split("/")) {
						if (String.valueOf(no).equals(v)) {
							visited = true;
						}
					}
				}
			}
		}
		
		if (!visited) {
			new BoardDao().hitByNo(no);
			
			Cookie cookie = new Cookie("viewsList", views+String.valueOf(no)+"/");
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(24 * 60 * 60);
			response.addCookie(cookie);
		}
		
		request.setAttribute("boardVo", new BoardDao().findViewByNo(no));
		request.setAttribute("p", p);
		request
			.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
	}

}