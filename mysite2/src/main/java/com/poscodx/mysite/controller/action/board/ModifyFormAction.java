package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}

		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		if(request.getParameter("no") == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		Long no = Long.parseLong(request.getParameter("no"));
		
		BoardVo vo = new BoardDao().findViewByNo(no);
		if(vo == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		if(!vo.getUserNo().equals(authUser.getNo())) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		request.setAttribute("boardVo", vo);
		request.getRequestDispatcher("/WEB-INF/views/board/modify.jsp").forward(request, response);
	}

}
