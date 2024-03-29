package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import DTO.Diary;
import DTO.ToDoList;
import mysql.DBConnection;

public class ToDoListDAO {

	public ToDoListDAO() {
	}

	/**
	 * @author 서혜리<br>
	 *         insertToDoList : todolist를 DB에 저장하는 기능<br>
	 * 
	 * @return result : db에 저장이 성공적으로 되면 1을 반환
	 */
	public static int insertToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into todolist (userId, title, content, createDate, closedDate, priority) ";
			sql += "values (?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, toDoList.getUserId());
			pstmt.setString(2, toDoList.getTitle());
			pstmt.setString(3, toDoList.getContent());
			pstmt.setDate(4, Date.valueOf(LocalDate.now()));
			pstmt.setString(5, toDoList.getClosedDate());
			pstmt.setInt(6, toDoList.getPriority());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}

	/**
	 * @author 서혜리<br>
	 *         deleteToDoList : todolist에 있는 isDeleted 컬럼의 값을 1로 반환하는 기능<br>
	 *         따라서 조회할 때 isDeleted 값이 1이면 조회되지 않도록 함<br>
	 * 
	 * @return result : db에서 삭제가 성공적으로 되면 1을 반환
	 */
	public static int deleteToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "update todolist set isDeleted = 1 where userId = ? and title = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, toDoList.getUserId());
			pstmt.setString(2, toDoList.getTitle());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}

	/**
	 * @author 서혜리<br>
	 *         updateToDoList : todolist를 수정하는 기능<br>
	 * 
	 * @return result : db에 저장이 성공적으로 되면 1을 반환
	 */
	public static int updateToDoList(Connection conn, ToDoList toDoList) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "update todolist set title = ?, content=?, closedDate = ?, priority = ?, isComplete = ? where userId = ? and title = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, toDoList.getNewTitle());
			pstmt.setString(2, toDoList.getContent());
			pstmt.setString(3, toDoList.getClosedDate());
			pstmt.setInt(4, toDoList.getPriority());
			pstmt.setBoolean(5, toDoList.getIsComplete());
			pstmt.setInt(6, toDoList.getUserId());
			pstmt.setString(7, toDoList.getTitle());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("ToDoListDAO: " + result);
		return result;
	}

	/**
	 * @author orbit getTodoList<br>
	 *         : todolist DB에서 불러오는 기능<br>
	 *         filter param에 따라 정렬<br>
	 * 
	 * @param String filter<br>
	 *               all = 전체 내용 가져오기(종료 날짜 순 & 중요도 순)<br>
	 *               completed = 완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)<br>
	 *               incomplete = 종료한 리스트 불러오기(종료 날짜 순 & 중요도 순) <br>
	 * @return todoList
	 * @throws SQLException
	 */
	public static List<ToDoList> getTodoList(String filter, ToDoList toDoList) throws SQLException {
		List<ToDoList> todoList = new LinkedList<>();
		String sql;
		PreparedStatement ps = null;
		try {
			if ("all".equals(filter)) {
				// 전체 내용 가져오기(종료 날짜 순 & 중요도 순)
				sql = "SELECT * FROM todolist WHERE userId = ? and isDeleted = 0 ORDER BY closedDate ASC, priority DESC";
			} else if ("completed".equals(filter)) {
				// 완료한 리스트 불러오기(종료 날짜 순 & 중요도 순)
				sql = "SELECT * FROM todolist WHERE userId = ? and isDeleted = 0 and isComplete = 1 ORDER BY closedDate ASC, priority DESC";
			} else if ("incomplete".equals(filter)) {
				// 종료한 리스트 불러오기(종료 날짜 순 & 중요도 순)
				sql = "SELECT * FROM todolist WHERE userId = ? and isDeleted = 0 and isComplete = 0 ORDER BY closedDate ASC, priority DESC";
			} else {
				throw new IllegalArgumentException(
						"Invalid filter value. Supported values: 'all', 'completed', 'incomplete'");
			}
			ps = DBConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, toDoList.getUserId());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				System.out.println("조회할 데이터가 없습니다.");
			} else {
				do {
					Integer toDoListId = rs.getInt("toDoListId");
					Integer userId = rs.getInt("userId");
					String title = rs.getString("title");
					String content = rs.getString("content");
					LocalDate createDate = rs.getDate("createDate").toLocalDate();
					String closedDate = rs.getString("closedDate");
					Integer priority = rs.getInt("priority");
					Boolean isComplete = rs.getBoolean("isComplete");
					Boolean isDeleted = rs.getBoolean("isDeleted");

					ToDoList list = new ToDoList(toDoListId, userId, title, content, createDate, closedDate, priority,
							isComplete, isDeleted);

					System.out.printf("%s", list);
					System.out.printf("---------------------------------\n");

					todoList.add(list);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
		}
		return todoList;
	}
}
