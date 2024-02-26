package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DTO.Diary;
import mysql.DBConnection;

public class DiaryDAO {

	/**
	 * @author 권재원 일기 작성
	 */
	public static int writeDiary(Connection conn, Diary diary) throws Exception {
		String sql;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			sql = "INSERT INTO DIARY (title, content, userId)";
			sql += " VALUES (?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, diary.getTitle());
			pstmt.setString(2, diary.getContent());
			pstmt.setInt(3, diary.getUserId());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}

		return result;
	}

	/**
	 * @author 권재원 일기 단건 삭제
	 */
	public static int deleteDiary(Connection conn, Diary diary) throws Exception {
		String sql;
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			sql = "UPDATE DIARY SET isDeleted = 1 WHERE userId = ? AND title = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, diary.getUserId());
			pstmt.setString(2, diary.getTitle());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}

		System.out.println("diaryDAO : " + result);
		return result;
	}

	/**
	 * @author 김동우
	 *
	 *         updateDiary : diary 수정
	 *
	 *         result : db에 저장이 성공적으로 되면 1을 반환
	 *
	 *         일기 수정
	 */
	public static int updateDiary(Connection conn, Diary diary) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "UPDATE DIARY SET title = ?, content = ? WHERE userId = ? AND title = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, diary.getNewTitle());
			pstmt.setString(2, diary.getContent());
			pstmt.setInt(3, diary.getUserId());
			pstmt.setString(4, diary.getTitle());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		System.out.println("DiaryDAO: " + result);
		return result;
	}

	/**
	 * @author 김동우 전체 일기 조회 입력 받은 특정 날짜 대한 일기 조회하기
	 * @param filter   조회할 일기의 조건 ("all": 전체 일기 조회, "specDate": 특정 날짜에 해당하는 일기 조회)
	 * @param specDate 특정 날짜를 조회할 경우 해당 날짜, 그 외의 경우 null
	 * @return 조회된 일기 리스트
	 */
	public static List<Diary> getDiary(String filter, Diary diary) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		List<Diary> diaryList = new ArrayList<>();
		try {
			if (filter.equals("all")) {
				// 모든 일기 가져오기

				sql = "SELECT * FROM diary WHERE userId = ? AND isDeleted = 0 ORDER BY createDate ASC ";
				pstmt = DBConnection.getConnection().prepareStatement(sql);
				pstmt.setInt(1, diary.getUserId());

			} else if (filter.equals("specdate")) {
				// 특정 날짜 입력 받고 해당하는 일기 가져오기
				sql = "SELECT * FROM diary WHERE userId = ? AND createDate = ? AND isDeleted = 0 ORDER BY createDate ASC";
				pstmt = DBConnection.getConnection().prepareStatement(sql);
				pstmt.setInt(1, diary.getUserId());
				pstmt.setString(2, (diary.getCreateDate()));

			} else {
				throw new IllegalArgumentException("Invalid filter value. Supported values: 'all', 'specdate'");
			}
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				System.out.println("조회할 데이터가 없습니다.");

			} else {
				do {
					Integer diaryId = rs.getInt("diaryId");
					String title = rs.getString("title");
					String content = rs.getString("content");
					Integer userId = rs.getInt("userId");
					String createDate = rs.getString("createDate");
					Boolean isDeleted = rs.getBoolean("isDeleted");

					Diary getDiary = new Diary(diaryId, title, content, userId, createDate, isDeleted);

					System.out.printf("%s", getDiary);
					System.out.println("---------------------------------");

					diaryList.add(getDiary);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return diaryList;
	}
}
