package com.poscodx.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.poscodx.mysite.vo.UserVo;

public class UserDao {
	private Connection connection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.192:3306/webdb?charset=utf-8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:"+e);
		}
		
		return conn;
	}
	
	public int insert(UserVo vo) {
		int result = 0;
		
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("insert into user values(null, ?, ?, password(?), ?, current_date())");
				) {
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}

	public UserVo findByNoAndPasssword(String email, String password) {
		UserVo result = null;
		
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select no, name from user where email = ? and password=password(?)");
				) {
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				
				result = new UserVo();
				result.setNo(no);
				result.setName(name);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}

	public UserVo findByNo(Long no) {
		UserVo result = null;
		
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select name, email from user where no = ?");
				) {
			
			pstmt.setLong(1, no);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString(1);
				String email = rs.getString(2);
				
				result = new UserVo();
				result.setName(name);
				result.setEmail(email);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}
}
