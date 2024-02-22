package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DTO.User;

public class UserDAO {
	public boolean insertUser(Connection connection, User user)
			throws SQLException {
		String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());

			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public boolean loginUser(Connection connection, User user) throws SQLException {
		String loginQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(loginQuery)) {
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());

			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		}
	}
}
