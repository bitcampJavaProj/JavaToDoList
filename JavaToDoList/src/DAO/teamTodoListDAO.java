package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import DTO.DBConnection;

/**
 * teamTodoListDAO
 * : teamTodoList sorting용
 * 
 * @author orbit
 * @todo 24.02.22
 */
public class teamTodoListDAO {
    public static List<teamTodoList> getAllTodoList() {
		List <teamTodoList> todoList = new LinkedList <>();
	    try {
	        String sql = "SELECT * from teamtodolist";
	        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while(rs.next()) {
				Integer teamId = rs.getInt("teamId");
				String title = rs.getString("title"); 
				String content = rs.getString("content");
				java.util.Date createDate = rs.getDate("createDate");
				java.util.Date closedDate = rs.getDate("closedDate");
				Integer priority = rs.getInt("priority");
				Boolean isComplete = rs.getBoolean("isComplete");
				Boolean isDeleted = rs.getBoolean("isDeleted");
				
	            teamTodoList list = new teamTodoList(teamId, title, content, createDate, closedDate, priority, isComplete, isDeleted);
	            System.out.println(list);
	            
	            todoList.add(list);
	        }
	    } catch(SQLException throwables) {
	        throwables.printStackTrace();
	    }
	    return todoList;
	}
    
    public static void main(String[] args) {
        DBConnection.openConnection();
        
		List <teamTodoList> todoList = new LinkedList <>();
		getAllTodoList();
		
        DBConnection.closeConnection();
	}
}
