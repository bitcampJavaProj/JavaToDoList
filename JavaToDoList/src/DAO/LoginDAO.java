package DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

import mysql.DBConnection;

public class LoginDAO {
    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                Connection conn = DBConnection.getConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // 클라이언트로부터 요청 받음
                String request = reader.readLine();

                // UserDAO 인스턴스 생성
                UserDAO userDAO = new UserDAO();

                if (request.equals("회원 가입")) {
                    // 회원 가입 요청 처리
                    String username = reader.readLine();
                    String password = reader.readLine();

                    if (userDAO.insertUser(username, password) == 1) {
                        writer.println("가입 성공");
                    } else {
                        writer.println("가입 실패");
                    }
                    
                } else if (request.equals("로그인")) {
                    // 로그인 요청 처리
                    String username = reader.readLine();
                    String password = reader.readLine();

                    if (userDAO.loginUser(username, password) == 1) {
                        writer.println("로그인 성공");
                    } else {
                        writer.println("로그인 실패");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}