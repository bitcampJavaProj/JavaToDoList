package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
    	// 클라이언트가 서버 연결 후 서버 데이터 불러오기, 사용자 입력을 서버에 전송
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {
        	
        	//환영 메세지 및 옵션 안내
            System.out.println("*옵션 선택");
            System.out.println("1. 회원 가입");
            System.out.println("2. 로그인");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // 회원 가입
                writer.println("SIGNUP");
                System.out.print("Enter your username: ");
                writer.println(scanner.nextLine());
                System.out.print("Enter your name: ");
                writer.println(scanner.nextLine());
                System.out.print("Enter your password: ");
                writer.println(scanner.nextLine());

                // 서버로부터 회원 가입 결과 수신
                String signupResult = reader.readLine();
                System.out.println(signupResult);
            } else if (choice == 2) {
                // 로그인
                writer.println("LOGIN");
                System.out.print("Enter your username: ");
                writer.println(scanner.nextLine());
                System.out.print("Enter your password: ");
                writer.println(scanner.nextLine());

                // 서버로부터 로그인 결과 수신
                String loginResult = reader.readLine();
                System.out.println(loginResult);
            } else {
                System.out.println("Invalid choice.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}