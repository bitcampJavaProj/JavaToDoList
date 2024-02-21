package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DAO.*;
import mysql.DBConnection;

public class Server {
	
	private static final int PORT = 9999;

    public static void main(String[] args) {
        // DB 연결 초기화
        DBConnection.openConnection();
        
        DBConnection.closeConnection();
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "your_mysql_username", "your_mysql_password");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // 클라이언트로부터 요청 받음
                String request = reader.readLine();
                
                // UserDAO 인스턴스 생성
                UserDAO userDAO = new UserDAO();

                if (request.equals("회원 가입")) {
                    // 회원 가입 요청 처리
                    String userId = reader.readLine();
                    String userName = reader.readLine();
                    String password = reader.readLine();

                    if (userDAO.insertUser(connection, userId, userName, password)) {
                        writer.println("가입 성공");
                    } else {
                        writer.println("가입 실패");
                    }
                } else if (request.equals("로그인")) {
                    // 로그인 요청 처리
                    String userId = reader.readLine();
                    String password = reader.readLine();

                    if (userDAO.loginUser(connection, userId, password)) {
                        writer.println("로그인 성공");
                    } else {
                        writer.println("로그인 실패");
                    }
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
