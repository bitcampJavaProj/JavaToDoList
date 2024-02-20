package server;

import mysql.DBConnection;

public class Server {
	public static void main(String[] args) {
		DBConnection.openConnection();
		
		// 여기에 기능을 넣으면 됩니다.

		DBConnection.closeConnection();
	}
}
