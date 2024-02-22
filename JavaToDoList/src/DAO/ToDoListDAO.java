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

	/**
	 * @author 서혜리
	 * 
	 * insertToDoList
	 * : todolist를 DB에 저장하는 기능
	 * 
	 * result
	 * : db에 저장이 성공적으로 되면 1을 반환
	 * 
	 * @return result
	 */
	public int insertToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "insert into todolist (userId, title, content, createDate, closedDate, priority) ";
			sql += "values (?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, toDoList.getUserId());
			pstmt.setString(2, toDoList.getTitle());
			pstmt.setString(3, toDoList.getContent());
			pstmt.setDate(4, Date.valueOf(LocalDate.now()));
			pstmt.setString(5, toDoList.getClosedDate());
			pstmt.setInt(6, toDoList.getPriority());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}
	
	/**
	 * @author 서혜리
	 * 
	 * deleteToDoList
	 * : todolist를 삭제하는 기능
	 * 
	 * result
	 * : db에서 삭제가 성공적으로 되면 1을 반환
	 * 
	 * @return result
	 */
	public int deleteToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "update todolist set isDeleted = 1 where userId = ? and toDoListId = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, toDoList.getUserId());
			pstmt.setInt(2, toDoList.getToDoListId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}
	
	/**
	 * @author 서혜리
	 * 
	 * updateToDoList
	 * : todolist를 수정하는 기능
	 * 
	 * result
	 * : db에 저장이 성공적으로 되면 1을 반환
	 * 
	 * @return result
	 */
	public int updateToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		try {
			sql = "update todolist set title = ?, content=?, closedDate = ?, priority = ?, isComplete = ? where userId = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, toDoList.getTitle());
			pstmt.setString(2, toDoList.getContent());
			pstmt.setString(3, toDoList.getClosedDate());
			pstmt.setInt(4, toDoList.getPriority());
			pstmt.setBoolean(5, toDoList.getIsComplete());
			pstmt.setInt(6, toDoList.getUserId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}
	
	/**
	 * @author orbit
	 * getTodoList<br>
	 * : todolist DB에서 불러오는 기능<br>
	 * filter param에 따라 정렬<br>
	 * 
	 * @param String filter<br>
	 * all =  전체 내용 가져오기(종료 날짜 순 & 중요도 순)<br>
	 * completed =  완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)<br>
	 * incomplete =  종료한 리스트 불러오기(종료 날짜 순 & 중요도 순) <br>
	 * @return todoList
	 */
	public List<ToDoList> getTodoList(String filter) {
		List<ToDoList> todoList = new LinkedList<>();
		try {
	        if ("all".equals(filter)) {
	            // 전체 내용 가져오기(종료 날짜 순 & 중요도 순)
	            sql = "SELECT * FROM todolist ORDER BY closedDate ASC, priority DESC";
	        } else if ("completed".equals(filter)) {
	            // 완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)
	            sql = "SELECT * FROM todolist WHERE isComplete = 1 ORDER BY closedDate ASC, priority DESC";
	        } else if ("incomplete".equals(filter)) {
	            // 종료한 리스트 불러오기(종료 날짜 순 & 중요도 순) 
	            sql = "SELECT * FROM todolist WHERE isComplete = 0 ORDER BY closedDate ASC, priority DESC";
	        } else {
	            throw new IllegalArgumentException("Invalid filter value. Supported values: 'all', 'completed', 'incomplete'");
	        }
			
			PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Integer toDoListId = rs.getInt("toDoListId");
				Integer teamId = rs.getInt("teamId");
				String title = rs.getString("title");
				String content = rs.getString("content");
				LocalDate createDate = rs.getDate("createDate").toLocalDate();
				String closedDate = rs.getString("closedDate");
				Integer priority = rs.getInt("priority");
				Boolean isComplete = rs.getBoolean("isComplete");
				Boolean isDeleted = rs.getBoolean("isDeleted");

				ToDoList list = new ToDoList(
						toDoListId,
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
