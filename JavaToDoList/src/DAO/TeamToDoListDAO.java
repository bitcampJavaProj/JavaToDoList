package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import DTO.TeamToDoList;

public class TeamToDoListDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private String sql;
	
	public TeamToDoListDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/thisisjava",
					"javaProj",
					"20240219!!"
			);
			pstmt = null;
			sql = "";
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Method */
	//insert
	public int insertToDoList(TeamToDoList toDoList) throws Exception {
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
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
	//delete
	public int deleteToDoList(TeamToDoList toDoList) throws Exception {
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
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
	//update
	public int updateToDoList(TeamToDoList toDoList) throws Exception {
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
		}
		System.out.println("TeamToDoListDAO: " + result);
		return result;
	}
	
}
