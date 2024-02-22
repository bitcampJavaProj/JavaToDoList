package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import DTO.Cmd;
import DTO.User;

public class Client {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 9999;

	public static void main(String[] args) throws Exception {
		// 클라이언트가 서버 연결 후 서버 데이터 불러오기, 사용자 입력을 서버에 전송
		Socket socket = new Socket(SERVER_IP, SERVER_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Scanner scanner = new Scanner(System.in);
		try {
			boolean isLoggedIn = false;

			while (true) {
				if (!isLoggedIn) {
					// 로그인하지 않은 경우
					displayLoginMenu(scanner);
					int loginChoice = scanner.nextInt();
					scanner.nextLine(); // Consume newline

					switch (loginChoice) {
					case ServiceMenu.회원가입:
						oos.writeObject(handleSignUp(scanner));
						
						break;
					case ServiceMenu.로그인:
						isLoggedIn = handleLogin(scanner);
						break;
					case ServiceMenu.로그아웃:
						System.out.println("프로그램 종료");
						return;
					default:
						System.out.println("1~3 사이의 값을 입력해주세요.");
						break;
					}
				} else {
					// 로그인한 경우
					displayMainMenu(scanner);
					int mainMenuChoice = scanner.nextInt();
					scanner.nextLine(); // Consume newline

					switch (mainMenuChoice) {
					case ServiceMenu2.투두리스트_작성:
						handleToDoListCreation(scanner);
						break;
					case ServiceMenu2.투두리스트_삭제:
						
						break;
					case ServiceMenu2.투두리스트_수정:
						handleToDoListRetrieval(scanner);
						break;
					case ServiceMenu2.투두리스트_전체_조회:
						
						break;
					case ServiceMenu2.투두리스트_완료:
						
						break;
					case ServiceMenu2.다이어리_작성:
						handleDiaryEntry(scanner);
						break;
					case ServiceMenu2.다이어리_삭제:
						handleDiaryDeletion(scanner);
						break;
					case ServiceMenu2.다이어리_수정:
						break;
					case ServiceMenu2.다이어리_전체_조회:
						handleDiaryRetrieval(scanner);
						break;
					case ServiceMenu2.다이어리_특정날짜:
						break;
					default:
						System.out.println("1~11 사이의 값을 입력해주세요.");
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayLoginMenu(Scanner scanner) {
		System.out.println("----------옵션 선택----------");
		System.out.println("1. 회원 가입");
		System.out.println("2. 로그인");
		System.out.println("3. 로그아웃");
		System.out.println();
	}

	private static User handleSignUp(Scanner scanner) throws IOException {
		// 회원 가입 처리
		System.out.println("----------회원가입----------");
		System.out.print("사용하실 ID를 입력해주세요: ");
		String username = scanner.nextLine();
		System.out.print("비밀번호를 입력해주세요: ");
		String password = scanner.nextLine();
		
		// 회원가입 정보를 서버로 전송
		Cmd cmd = new Cmd(ServiceMenu.회원가입);
		User user = new User(username, password);
		return user;
	}

	private static boolean handleLogin(Scanner scanner) throws IOException {
		// 로그인 처리
		System.out.println("----------로그인----------");
		System.out.print("ID를 입력해주세요: ");
		writer.println(scanner.nextLine());
		System.out.print("비밀번호를 입력해주세요: ");
		writer.println(scanner.nextLine());

		// 서버로부터 로그인 결과 수신
		String loginResult = reader.readLine();
		System.out.println(loginResult);
		return loginResult.equals("로그인 성공");
	}

	private static void displayMainMenu(Scanner scanner) {
		System.out.println("----------옵션 선택----------");
		System.out.println("1. 투두리스트 작성");
		System.out.println("2. 투두리스트 삭제");
		System.out.println("3. 투두리스트 수정");
		System.out.println("4. 투두리스트 전체 조회");
		System.out.println("5. 완료된 투두리스트 조회");
		System.out.println("6. 미완료된 투두리스트 조회");
		System.out.println("7. 일기 작성");
		System.out.println("8. 일기 삭제");
		System.out.println("9. 일기 수정");
		System.out.println("10. 일기 전체 조회");
		System.out.println("11. 작성 날짜로 일기 조회");
		System.out.println();
	}

	private static void handleToDoListCreation(Scanner scanner)
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

	private static void handleToDoListRetrieval(Scanner scanner)
			throws IOException {
		// 투두리스트 조회 처리
		writer.println("----------투두리스트 조회----------");
		// 서버로부터 결과 수신 및 출력
	}

	private static void handleDiaryEntry(Scanner scanner)
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

	private static void handleDiaryRetrieval(Scanner scanner)
			throws IOException {
		// 일기 조회 처리
		writer.println("----------일기 조회----------");
		// 서버로부터 결과 수신 및 출력
	}

	private static void handleDiaryDeletion(Scanner scanner)
			throws IOException {
		// 일기 삭제 처리
		writer.println("----------일기 삭제----------");
		// 서버로부터 결과 수신 및 출력
	}
}