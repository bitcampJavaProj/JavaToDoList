package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import DTO.ToDoList;
import mysql.DBConnection;

public class ToDoListDAO {
	private PreparedStatement pstmt;
	private String sql;
	
	public ToDoListDAO() {}
	
	/* Method */
	// @author hyeri 투두리스트 작성
	public int insertToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "insert into teamtodolist (title, content, createDate, closedDate, priority, isComplete, isDelete) ";
			sql += "values (?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toDoList.getTitle());
			pstmt.setString(2, toDoList.getContent());
			pstmt.setDate(3, Date.valueOf(LocalDate.now()));
			pstmt.setString(4, toDoList.getClosedDate());
			pstmt.setInt(5, toDoList.getPriority());
			pstmt.setBoolean(6, toDoList.getIsComplete());
			pstmt.setBoolean(7, toDoList.getIsDeleted());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
			conn.close();
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
	// @author hyeri 투두리스트 삭제
	public int deleteToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "update teamtodolist set isDeleted = 1 where teamId = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, toDoList.getTeamId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
			conn.close();
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
	// @author hyeri 투두리스트 수정
	public int updateToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "update teamtodolist set title = ?, content=?, closedDate = ?, priority = ?, isComplete = ? where teamId = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toDoList.getTitle());
			pstmt.setString(2, toDoList.getContent());
			pstmt.setString(3, toDoList.getClosedDate());
			pstmt.setInt(4, toDoList.getPriority());
			pstmt.setBoolean(5, toDoList.getIsComplete());
			pstmt.setInt(6, toDoList.getTeamId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
			conn.close();
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
	/**
	 * getTodoList
	 * : todolist DB에서 불러오는 기능
	 * filter param에 따라 정렬
	 * 
	 * @param String filter
	 * all =  전체 내용 가져오기(종료 날짜 순 & 중요도 순)
	 * completed =  완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)
	 * incomplete =  종료한 리스트 불러오기(종료 날짜 순 & 중요도 순) 
	 * @return todoList
	 */
	public static List<ToDoList> getTodoList(String filter) {
		List<ToDoList> todoList = new LinkedList<>();
		try {
	        String sql;

	        if ("all".equals(filter)) {
	            // 전체 내용 가져오기(종료 날짜 순 & 중요도 순)
	            sql = "SELECT * FROM teamtodolist ORDER BY closedDate ASC, priority DESC";
	        } else if ("completed".equals(filter)) {
	            // 완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)
	            sql = "SELECT * FROM teamtodolist WHERE isComplete = 1 ORDER BY closedDate ASC, priority DESC";
	        } else if ("incomplete".equals(filter)) {
	            // 종료한 리스트 불러오기(종료 날짜 순 & 중요도 순) 
	            sql = "SELECT * FROM teamtodolist WHERE isComplete = 0 ORDER BY closedDate ASC, priority DESC";
	        } else {
	            throw new IllegalArgumentException("Invalid filter value. Supported values: 'all', 'completed', 'incomplete'");
	        }
			
			PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Integer teamId = rs.getInt("teamId");
				String title = rs.getString("title");
				String content = rs.getString("content");
				LocalDate createDate = rs.getDate("createDate").toLocalDate();
				String closedDate = rs.getString("closedDate");
				Integer priority = rs.getInt("priority");
				Boolean isComplete = rs.getBoolean("isComplete");
				Boolean isDeleted = rs.getBoolean("isDeleted");

				ToDoList list = new ToDoList(
						teamId, 
						title, 
						content, 
						createDate, 
						closedDate, 
						priority,
						isComplete, 
						isDeleted
						);
				
				todoList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(todoList);
		
		return todoList;
	}
}
