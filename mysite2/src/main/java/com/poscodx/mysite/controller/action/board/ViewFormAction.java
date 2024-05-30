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
		Long no = Long.parseLong(request.getParameter("no"));
		
		boolean visited = false;
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for(Cookie cookie : cookies) {
				if("visitView".equals(cookie.getName().substring(0, 9))) {
					if (Long.parseLong(cookie.getValue()) == no) {
						visited = true;
					}
				}
			}
		}
		
		if (!visited) {
			new BoardDao().hitByNo(no);
			
			Cookie cookie = new Cookie("visitView" + String.valueOf(no), String.valueOf(no));
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(24 * 60 * 60);
			response.addCookie(cookie);
		}
		
		request.setAttribute("boardVo", new BoardDao().findViewByNo(no));
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
	}

}