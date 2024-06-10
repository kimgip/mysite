package com.poscodx.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	private SqlSession sqlSession;
	
	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int hitByNo(Long no) {
		return sqlSession.update("board.hitByNo", no);
	}
	
	public int insert(BoardVo vo) {
		return sqlSession.insert("board.insert", vo);
	}
	
	public int findTotalCount(String keyword) {
		return sqlSession.selectOne("board.findTotalCount", keyword);
	}

	public List<BoardVo> findViewList(Long currentPage, String keyword, int listSize) {
		return sqlSession.selectList("board.findViewList", Map.of(
					"keyword", keyword,
					"viewNo", listSize * (currentPage - 1),
					"listSize", listSize
				));
	}

	public BoardVo findViewByNo(Long no) {
		return sqlSession.selectOne("board.findViewByNo", no);
	}
	
	public BoardVo findViewInfoByNo(Long no) {
		// no, g_no, o_no, depth
		return sqlSession.selectOne("board.findViewInfoByNo", no);
	}
	
	public int deleteByNoAndUserNo(Long boardNo, Long userNo) {
		return sqlSession.delete("board.deleteByNoAndUserNo", Map.of(
				"boardNo", boardNo,
				"userNo", userNo
				));
	}

	public int updateOrderNo(BoardVo vo) {
		return sqlSession.update("board.updateOrderNo", vo);
	}
	
	public int update(BoardVo vo) {
		return sqlSession.update("board.update", vo);
	}
}
