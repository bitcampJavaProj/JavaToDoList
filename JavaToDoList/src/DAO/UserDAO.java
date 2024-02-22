package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
	public boolean insertUser(Connection connection, String userId, String username, String password)
			throws SQLException {
		String insertQuery = "INSERT INTO users (userId, username, password) VALUES (?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public boolean loginUser(Connection connection, String username, String password) throws SQLException {
		String loginQuery = "SELECT * FROM users WHERE userId = ? AND password = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		}
	}
}
