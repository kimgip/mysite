package com.poscodx.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
		
	@RequestMapping("")
	public String index(
			@RequestParam(value="p", required=true, defaultValue="1") Long pageNo, 
			@RequestParam(value="kwd", required=true, defaultValue="") String keyword, Model model) {
		Map<String, Object> map = boardService.getContentsList(pageNo, keyword);
		model.addAllAttributes(map);
		model.addAttribute("keyword", keyword);
		return "board/list";
	}
	
	@RequestMapping("/view")
	public String view(@RequestParam(value="n", required=true, defaultValue="1") Long viewNo, Model model) {
		BoardVo vo = boardService.getContents(viewNo);
		model.addAttribute("boardVo", vo);
		return "board/view";
	}

	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(Authentication authentication, BoardVo vo) {
		vo.setUserNo(((UserVo)authentication.getPrincipal()).getNo());
		if(vo.getNo() != null) {
			BoardVo parentVo = boardService.getViewInfo(vo.getNo());
			if (parentVo != null) {
				vo.setGroupNo(parentVo.getGroupNo());
				vo.setOrderNo(parentVo.getOrderNo());
				vo.setDepth(parentVo.getDepth());
			}
		}
		
		boardService.addContents(vo);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(Authentication authentication, @RequestParam(value="n", required=true, defaultValue="1") Long viewNo, Model model) {

		BoardVo vo =  boardService.getContents(viewNo, ((UserVo)authentication.getPrincipal()).getNo());
		if (vo == null) {
			return "redirect:/board";
		}
		model.addAttribute("boardVo", vo);
		return "board/modify";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(Authentication authentication, BoardVo vo) {

		vo.setUserNo(((UserVo)authentication.getPrincipal()).getNo());
		boardService.updateContents(vo);
		return "redirect:/board";
	}
	
	@RequestMapping("/delete")
	public String delete(Authentication authentication, @RequestParam(value="n", required=true, defaultValue="1") Long viewNo) {
		
		boardService.deleteContents(viewNo, ((UserVo)authentication.getPrincipal()).getNo());
		return "redirect:/board";
	}
	
}
