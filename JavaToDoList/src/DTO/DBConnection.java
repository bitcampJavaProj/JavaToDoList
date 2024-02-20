package DTO;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBConnection
 * : MySQL DB connection class
 * 
 * @author orbit
 */
public class DBConnection {
	// field
	private static final String protocol = "jdbc";
	private static final String vendor = ":mysql:";
	private static final String location = "//192.168.0.73:3306/";
	private static final String databaseName = "todoList"; // DB name
	private static final String userName = "javaProj"; // Username
	private static String password = "20240219!!"; // Password

	private static final String DB_URL = protocol + vendor + location + databaseName;
	private static final String jdbcUrl = DB_URL + "?serverTimezone = UTC";

	private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver ref

	public static Connection connection; // Connection Interface

	// DB connection open
	public static void openConnection() {
		try {
			Class.forName(driver); // Locate Driver
			connection = DriverManager.getConnection(jdbcUrl, userName, password);
			System.out.println("Connection successful!\n\n");
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	// DB connection close
	public static void closeConnection() {
		try {
			connection.close();
			System.out.println("\n\nConnection closed!");
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
		}
	}

	// Connection getter
	public static Connection getConnection() {
		return connection;
	}
}
