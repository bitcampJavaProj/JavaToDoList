package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import DTO.Diary;
import mysql.DBConnection;

public class DiaryDAO {

	private PreparedStatement pstmt;
	private String sql;

	/**
	 * @author 권재원 
	 * 일기 작성
	 */
	public int writeDiary(Connection conn, Diary diary) throws Exception {
		int result = 0;

		try {
			sql = "INSERT INTO DIARY (title, content, userId)";
			sql += " VALUES (?, ?, ?, ?)";

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
	 * @author 권재원 
	 * 일기 단건 삭제
	 */
	public int deleteDiary(Connection conn, Diary diary) throws Exception {
		int result = 0;

		try {
			sql = "UPDATE DIARY SET isDeleted = 1 WHERE userId = ? AND diaryId = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, diary.getUserId());
			pstmt.setInt(2, diary.getDiaryId());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}

		return result;
	}

	/**
	 * @author 김동우 
	 * 일기 수정
	 */
	public int updateDiary(Connection conn, Diary diary) throws Exception {
		int result = 0;
		try {
			String sql = "UPDATE diary SET title = ?, content = ? WHERER diaryId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, diary.getTitle());
			pstmt.setString(2, diary.getContent());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
		}
		return result;
	}

}
