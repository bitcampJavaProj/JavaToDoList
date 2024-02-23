package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DTO.User;
import mysql.DBConnection;

public class UserDAO {
	
	/**
	 * insertUser<br>
	 * 유저 생성 기능
	 * 
	 * @param username 유저 ID
	 * @param password 유저 PW
	 * @return 가입성공 여부 0 = false, 1 = true, 2 = 사용중인 아이디
	 */
	public static int insertUser(User user) throws Exception {
	    int result = 0;

	    // 동일한 유저 있는지 확인
	    if (isUsernameExists(user.getUsername())) {
	        result = 2; // 동일 유저 있을시
	        System.out.println("이미 사용중인 ID입니다. 다른 아이디를 작성해 주세요.");
	        return result;
	    }

	    String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
	    try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
	    	System.out.println(user.getUsername());
	    	
	        pstmt.setString(1, user.getUsername());
	        pstmt.setString(2, user.getPassword());
	        
	        result = pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    System.out.printf("\nUserDAO: %s\n\n", result == 0 ? "가입에 실패했습니다. 다시 시도해 주세요." : "가입되었습니다. 로그인을 진행해주세요.");
	    return result;
	}
	
	/**
	 * loginUser<br>
	 * 유저 로그인 기능
	 *
	 * @param username 유저 ID
	 * @param password 유저 PW
	 * @return 유저 로그인 성공시 유저 고유 ID 반환, 실패시 0 반환
	 */
	public static int loginUser(User user) throws Exception {
	    int userId = 0;
	    String sql = "SELECT userId FROM user WHERE username = ? AND password = ?";
	    try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
	    	pstmt.setString(1, user.getUsername());
	    	pstmt.setString(2, user.getPassword());

	        ResultSet resultSet = pstmt.executeQuery();

	        if (resultSet.next()) {
	            // 로그인 성공 시 유저 ID 설정
	            userId = resultSet.getInt("userId");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		System.out.printf("\nUserDAO: %s\n\n", userId == 0 ? "로그인에 실패했습니다. 다시 시도해 주세요." : "로그인에 성공했습니다.");
	    return userId;
	}
	
	/**
	 * isUsernameExists<br>
	 * 데이터 베이스에 이미 동일한 ID의 유저가 있는지 확인하는 기능
	 *
	 * @param username 유저 ID
	 * @return 있으면 true, 없으면 false
	 */
	private static boolean isUsernameExists(String username) {
	    String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
	    try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
	    	pstmt.setString(1, username);
	        ResultSet resultSet = pstmt.executeQuery();
	        resultSet.next();
	        int count = resultSet.getInt(1);
	        return count > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}
