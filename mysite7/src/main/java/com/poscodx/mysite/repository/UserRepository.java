package com.poscodx.mysite.repository;

import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poscodx.mysite.security.UserDetailsImpl;
import com.poscodx.mysite.vo.UserVo;

@Repository
public class UserRepository {
	private SqlSession sqlSession;
	
	public UserRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(UserVo vo) {
		return sqlSession.insert("user.insert", vo);
	}

	public UserVo findByEmailAndPasssword(String email, String password) {
		return sqlSession.selectOne(
				"user.findByEmailAndPasssword", 
				Map.of("email", email, "password", password));
	}

	public UserVo findByNo(Long no) {
		return sqlSession.selectOne("user.findByNo", no);
	}

	public <R> R findByEmail(String email, Class<R> resultType) {
		FindByEmailResultHandler<R> findByEmailResultHandler = new FindByEmailResultHandler<>(resultType);
		sqlSession.select("user.findByEmail", email, findByEmailResultHandler);
		return findByEmailResultHandler.result;
	}
	
	public int update(UserVo vo) {
		return sqlSession.update("user.update", vo);
	}
	
	private class FindByEmailResultHandler<R> implements ResultHandler<Map<String, Object>>{
		private R result;
		
		private Class<R> resultType;
		
		public FindByEmailResultHandler(Class<R> resultType) {
			this.resultType = resultType;
		}
		
		@Override
		public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
			Map<String, Object> resultMap = resultContext.getResultObject();
			this.result = new ObjectMapper().convertValue(resultMap, resultType);
		}
	}
}
