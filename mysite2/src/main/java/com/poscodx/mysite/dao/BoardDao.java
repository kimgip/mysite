package com.poscodx.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poscodx.mysite.vo.BoardVo;

public class BoardDao {
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
	
	public int hitByNo(Long no) {
		int result = 0;
		
		try (
				Connection conn = connection();
				) {
			PreparedStatement pstmt = conn.prepareStatement("update board set hit=hit+1 where no=?");
			
			pstmt.setLong(1, no);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}
	
	public int insert(String title, String contents, Long userNo) {
		int result = 0;
		
		try (
				Connection conn = connection();
				) {
			PreparedStatement pstmt = conn.prepareStatement("insert into board select null, ?, ?, 0, now(), max(g_no) + 1, 1, 0, ? from board");
			
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setLong(3, userNo);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}
	
	public int insert(String title, String contents, Long userNo, BoardVo parentVo) {
		int result = 0;
		
		update(parentVo.getGroupNo(), parentVo.getOrderNo());
		try (
				Connection conn = connection();
				) {
			PreparedStatement pstmt = conn.prepareStatement("insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?)");
			
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setLong(3, parentVo.getGroupNo());
			pstmt.setLong(4, parentVo.getOrderNo()+1);
			pstmt.setLong(5, parentVo.getDepth()+1);
			pstmt.setLong(6, userNo);

			result = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}
	
	public int findALLViewCount() {
		int result = 0;
		
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board");

				) {
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				result = (int)rs.getLong(1);
				
			}
			
			rs.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}

	public List<BoardVo> findViews(Long pageNo, Long viewCount) {
		List<BoardVo> result = new ArrayList<>();
		
		try (
				Connection conn = connection();

				) {
			PreparedStatement pstmt = conn.prepareStatement("select b.no, title, hit, date_format(reg_date, '%Y-%m-%d %H:%i:%s'), g_no, o_no, depth, a.name  from user a join board b on a.no = b.user_no order by g_no DESC, o_no limit ?, ?");
			pstmt.setLong(1, viewCount * (pageNo - 1));
			pstmt.setLong(2, viewCount);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				int hit = (int)rs.getLong(3);
				String regDate = rs.getString(4);
				int groupNo = (int)rs.getLong(5);
				int orderNo = (int)rs.getLong(6);
				int depth = (int)rs.getLong(7);
				String userName = rs.getString(8);
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, groupNo, orderNo, depth, userName);
				
				result.add(vo);
			}
			
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		
		return result;
	}

	public BoardVo findViewByNo(Long no) {
		BoardVo result = null;
		
		ResultSet rs = null;
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select title, contents, a.no from user a join board b on a.no = b.user_no where b.no = ?");
				) {
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);
				Long userNo = rs.getLong(3);
				
				result = new BoardVo(no, title, contents, userNo);
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		return result;
	}
	
	public BoardVo findStatusByNo(Long no) {
		BoardVo result = null;
		
		ResultSet rs = null;
		try (
				Connection conn = connection();
				PreparedStatement pstmt = conn.prepareStatement("select g_no, o_no, depth from user a join board b on a.no = b.user_no where b.no = ?");
				) {
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int groupNo = (int)rs.getLong(1);
				int orderNo = (int)rs.getLong(2);
				int depth = (int)rs.getLong(3);
				
				result = new BoardVo();
				result.setGroupNo(groupNo);
				result.setOrderNo(orderNo);
				result.setDepth(depth);
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}
		return result;
	}
	
	public int deleteByNoAndUserNo(Long no, Long userNo) {
		int result = 0;
		
		try (
				Connection conn = connection();
				) {
			
			String sql = "delete from board where no = ? and user_no = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			pstmt.setLong(2, userNo);
			
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}		
		return result;
	}

	private int update(int groupNo, int orderNo) {
		int result = 0;
		
		try (
				Connection conn = connection();
				) {
			
			String sql = "update board set o_no=o_no+1 where g_no=? and o_no>?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, groupNo);
			pstmt.setLong(2, orderNo);
			
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}		
		return result;
	}
	
	public int update(Long no, Long userNo, String title, String contents) {
		int result = 0;
		
		try (
				Connection conn = connection();
				) {
			
			String sql = "update board set title=?, contents=?, reg_date=now() where no=? and user_no=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setLong(3, no);
			pstmt.setLong(4, userNo);
			
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("error:"+e);
		}		
		return result;
	}
}
