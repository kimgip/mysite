package com.poscodx.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.security.Auth;
import com.poscodx.mysite.security.AuthUser;
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

	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(@AuthUser UserVo authUser, BoardVo vo) {
		vo.setUserNo(authUser.getNo());
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
	
	@Auth
	@RequestMapping(value="/modify", method=RequestMethod.GET)
	public String modify(@AuthUser UserVo authUser, @RequestParam(value="n", required=true, defaultValue="1") Long viewNo, Model model) {

		BoardVo vo =  boardService.getContents(viewNo, authUser.getNo());
		if (vo == null) {
			return "redirect:/board";
		}
		model.addAttribute("boardVo", vo);
		return "board/modify";
	}
	
	@Auth
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(@AuthUser UserVo authUser, BoardVo vo) {

		vo.setUserNo(authUser.getNo());
		boardService.updateContents(vo);
		return "redirect:/board";
	}
	
	@Auth
	@RequestMapping("/delete")
	public String delete(@AuthUser UserVo authUser, @RequestParam(value="n", required=true, defaultValue="1") Long viewNo) {
		
		boardService.deleteContents(viewNo, authUser.getNo());
		return "redirect:/board";
	}
	
}
