package DTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import DAO.TeamTodoListRecord;
import mysql.DBConnection;

/**
 * TeamTodoList
 * : TeamTodoList 기능구현 DTO
 */
public class TeamTodoList {
	/**
	 * getAllTodoList
	 * : TeamTodoList 리스트 전체 불러오기 위해
	 * 
	 * @author orbit
	 */
	public static List<TeamTodoListRecord> getAllTodoList() {
		List<TeamTodoListRecord> todoList = new LinkedList<>();
		try {
			String sql = "SELECT * from teamtodolist";
			PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Integer teamId = rs.getInt("teamId");
				String title = rs.getString("title");
				String content = rs.getString("content");
				java.util.Date createDate = rs.getDate("createDate");
				java.util.Date closedDate = rs.getDate("closedDate");
				Integer priority = rs.getInt("priority");
				Boolean isComplete = rs.getBoolean("isComplete");
				Boolean isDeleted = rs.getBoolean("isDeleted");

				TeamTodoListRecord list = new TeamTodoListRecord(
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
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		
		return todoList;
	}

}
