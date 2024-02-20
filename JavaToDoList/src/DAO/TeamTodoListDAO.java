package DAO;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import DTO.TeamTodoList;
import mysql.DBConnection;

/**
 * teamTodoListDAO : teamTodoList sorting용
 * 
 * @author orbit
 * @todo 24.02.22
 */
public class TeamTodoListDAO {
	public static void main(String[] args) {
		DBConnection.openConnection();

		List<TeamTodoListRecord> todoList = new LinkedList<>();

		todoList = TeamTodoList.getAllTodoList();
		
		// JSON 라이브러리를 사용하여 리스트를 JSON 형식으로 변환
		JSONArray jsonArray = new JSONArray(todoList);

		// 변환된 JSON 배열을 화면에 출력
		System.out.println("TeamTodoList all data :\n");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			System.out.println("Team ID: " + jsonObject.getInt("teamId"));
			System.out.println("Title: " + jsonObject.getString("title"));
			System.out.println("Content: " + jsonObject.getString("content"));
			System.out.println("Create Date: " + jsonObject.getString("createDate"));
			System.out.println("Closed Date: " + jsonObject.getString("closedDate"));
			System.out.println("Priority: " + jsonObject.getInt("priority"));
			System.out.println("Is Complete: " + jsonObject.getBoolean("isComplete"));
			System.out.println("Is Deleted: " + jsonObject.getBoolean("isDeleted"));
			System.out.println("---------------");
		}

		DBConnection.closeConnection();
	}

	/**
	 * SortByDate : TeamTodoList 리스트를 날짜를 비교하기 위해
	 * 
	 * @author orbit
	 */
	static class SortByDate implements Comparator<TeamTodoListRecord> {
		@Override
		public int compare(TeamTodoListRecord a, TeamTodoListRecord b) {
			return a.getClosedDate().compareTo(b.getClosedDate());
		}
	}
}
