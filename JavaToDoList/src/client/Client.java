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
			
			boolean isLoggedIn = false;

			while (true) {
				if (!isLoggedIn) {
					// 로그인하지 않은 경우
					displayLoginMenu(scanner, writer, reader);
					int loginChoice = scanner.nextInt();
					scanner.nextLine(); // Consume newline

					switch (loginChoice) {
					case 1:
						isLoggedIn = handleSignUp(scanner, writer, reader);
						break;
					case 2:
						isLoggedIn = handleLogin(scanner, writer, reader);
						break;
					case 0:
						System.out.println("프로그램 종료");
						return;
					default:
						System.out.println("0~2 사이의 값을 입력해주세요.");
						break;
					}
				} else {
					// 로그인한 경우
					displayMainMenu(scanner, writer);
					int mainMenuChoice = scanner.nextInt();
					scanner.nextLine(); // Consume newline

					switch (mainMenuChoice) {
					case 1:
						handleToDoListCreation(scanner, writer, reader);
						break;
					case 2:
						handleToDoListRetrieval(scanner, writer, reader);
						break;
					case 3:
						handleDiaryEntry(scanner, writer, reader);
						break;
					case 4:
						handleDiaryRetrieval(scanner, writer, reader);
						break;
					case 5:
						handleDiaryDeletion(scanner, writer, reader);
						break;
					case 6:
						isLoggedIn = false; // 로그아웃
						break;
					default:
						System.out.println("0~6 사이의 값을 입력해주세요.");
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayLoginMenu(Scanner scanner, PrintWriter writer, BufferedReader reader) {
		System.out.println("----------옵션 선택----------");
		System.out.println("1. 회원 가입");
		System.out.println("2. 로그인");
		System.out.println("0. 종료");
		System.out.println();
	}

	private static boolean handleSignUp(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
		// 회원 가입 처리
		writer.println("----------회원가입----------");
		System.out.print("사용하실 ID를 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("비밀번호를 입력해주세요: ");
		writer.println(scanner.nextLine());

		// 서버로부터 회원 가입 결과 수신
		String signupResult = reader.readLine();
		System.out.println(signupResult);
		return signupResult.equals("회원가입이 완료되었습니다.");
	}

	private static boolean handleLogin(Scanner scanner, PrintWriter writer, BufferedReader reader) throws IOException {
		// 로그인 처리
		writer.println("----------로그인----------");
		System.out.print("ID를 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("비밀번호를 입력해주세요: ");
		writer.println(scanner.nextLine());

		// 서버로부터 로그인 결과 수신
		String loginResult = reader.readLine();
		System.out.println(loginResult);
		return loginResult.equals("로그인 성공");
	}

	private static void displayMainMenu(Scanner scanner, PrintWriter writer) {
		System.out.println("----------옵션 선택----------");
		System.out.println("1. 투두리스트 작성");
		System.out.println("2. 투두리스트 조회");
		System.out.println("3. 일기 작성");
		System.out.println("4. 일기 조회");
		System.out.println("5. 일기 삭제");
		System.out.println("6. 로그아웃");
		System.out.println();
	}

	private static void handleToDoListCreation(Scanner scanner, PrintWriter writer, BufferedReader reader)
			throws IOException {
		// 투두리스트 작성 처리
		writer.println("----------투두리스트 작성----------");
		System.out.print("제목을 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("할 일을 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("마감일을 입력해주세요(예: 2024-02-19): ");
		writer.println(scanner.nextLine());
		System.out.print("우선순위를 숫자로 입력해주세요: ");
		writer.println(scanner.nextInt());
		scanner.nextLine(); // Consume newline

		// 서버로부터 결과 수신
		String result = reader.readLine();
		System.out.println(result);
	}

	private static void handleToDoListRetrieval(Scanner scanner, PrintWriter writer, BufferedReader reader)
			throws IOException {
		// 투두리스트 조회 처리
		writer.println("----------투두리스트 조회----------");
		// 서버로부터 결과 수신 및 출력
	}

	private static void handleDiaryEntry(Scanner scanner, PrintWriter writer, BufferedReader reader)
			throws IOException {
		// 일기 작성 처리
		writer.println("----------일기 작성----------");
		System.out.print("제목을 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("내용을 입력해주세요: ");
		writer.println(scanner.nextLine());

		// 서버로부터 결과 수신
		String result = reader.readLine();
		System.out.println(result);
	}

	private static void handleDiaryRetrieval(Scanner scanner, PrintWriter writer, BufferedReader reader)
			throws IOException {
		// 일기 조회 처리
		writer.println("----------일기 조회----------");
		// 서버로부터 결과 수신 및 출력
	}

	private static void handleDiaryDeletion(Scanner scanner, PrintWriter writer, BufferedReader reader)
			throws IOException {
		// 일기 삭제 처리
		writer.println("----------일기 삭제----------");
		// 서버로부터 결과 수신 및 출력
	}
}