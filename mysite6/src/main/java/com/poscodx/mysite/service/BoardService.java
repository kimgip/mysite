package com.poscodx.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.BoardRepository;
import com.poscodx.mysite.vo.BoardVo;

@Service
public class BoardService {
	@Autowired
	BoardRepository boardRepository;
	
	public BoardVo getViewInfo(Long no) {
		return boardRepository.findViewInfoByNo(no);
	}
	
	public void addContents(BoardVo vo) {
		if(vo.getGroupNo() != 0) { // 답글
			boardRepository.updateOrderNo(vo);
		}
		boardRepository.insert(vo);
	}
	
	public BoardVo getContents(Long no) {
		// view
		boolean visited = false;
		
		if(!visited) {
			boardRepository.hitByNo(no);
		}
		
		return boardRepository.findViewByNo(no);
	}
	
	public BoardVo getContents(Long boardNo, Long userNo) {
		// modify view
		// no, title, contents, userNo
		BoardVo vo = boardRepository.findViewByNo(boardNo); 
		if (userNo != vo.getUserNo()) {
			vo = null;
		}
		return vo;
	}
	
	public void updateContents(BoardVo vo) {
		// modify
		boardRepository.update(vo);
	}
	
	public void deleteContents(Long boardNo, Long userNo) {
		boardRepository.deleteByNoAndUserNo(boardNo, userNo);
	}
	
	public Map<String, Object> getContentsList(Long currentPage, String keyword) {
		int listSize = 5;
		int pagerSize = 5;
		List<BoardVo> list = boardRepository.findViewList(currentPage, keyword, listSize);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("list", list);
		map.put("kyword", keyword);
		
		int totalCount = boardRepository.findTotalCount(keyword);
		
		int endPage = (int) Math.ceil(totalCount / listSize);
		int prevPage = ((int)((currentPage - 1) / pagerSize) * pagerSize) + 1;
		if (prevPage < 1) {
			prevPage = 1;
		}
		
		map.put("totalCount", totalCount);
//		map.put("listSize", listSize);
		map.put("currentPage", currentPage);
//		map.put("beginPage", 0);
		map.put("endPage", endPage);
		map.put("prevPage", prevPage);
//		map.put("nextPage", 0);
		map.put("pagerSize", pagerSize);
		map.put("topViewNo", totalCount - listSize * (currentPage - 1));

		return map;
	}
	
	
}
