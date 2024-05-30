package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.UserVo;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/board?a=viewform&no=" + request.getParameter("no"));
			return;
		}

		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/board?a=viewform&no=" + request.getParameter("no"));
			return;
		}
		
		Long no = Long.parseLong(request.getParameter("no"));
		request.setAttribute("boardVo", new BoardDao().findViewByNo(no));
		
		request.getRequestDispatcher("/WEB-INF/views/board/modify.jsp").forward(request, response);
	}

}
