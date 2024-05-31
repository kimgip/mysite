package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;

public class BoardAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long viewCount = 5L;
		Long pagerCount = 5L;
		String pageNo = request.getParameter("p");
		
		if (pageNo == null) {
			response.sendRedirect(request.getContextPath() + "/board?p=1");
			return;
		}
		
		Long currP = Long.parseLong(pageNo);
		
		if (currP <= 0) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
		request.setAttribute("pagerCnt", pagerCount);
		
		Long prevP = ((int)((currP - 1) / pagerCount) * pagerCount) + 1;
		int allViewCount = new BoardDao().findALLViewCount();
		int endP = (int)(allViewCount / viewCount);
		if (allViewCount % viewCount > 0) {
			endP++;
		}
		
		if (currP > endP) {
			response.sendRedirect(request.getContextPath() + "/board?p="+endP);
			return;
		}
		
		
		request.setAttribute("_1viewNo", allViewCount - viewCount * (currP - 1));
		request.setAttribute("prevP", prevP);
		request.setAttribute("endP", endP);
		request.setAttribute("list", new BoardDao().findViews(currP, viewCount));
		
		request
			.getRequestDispatcher("WEB-INF/views/board/list.jsp")
			.forward(request, response);
	}

}
