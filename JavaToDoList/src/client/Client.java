package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

import DAO.ToDoListDAO;
import DAO.UserDAO;
import DTO.Diary;
import DTO.ToDoList;
import DTO.User;

public class Client {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 9000;
	private static Integer userId;

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
					String loginChoice = scanner.nextLine();

					switch (loginChoice) {
					case "회원가입":
						// Cmd cmd = new Cmd(ServiceMenu.회원가입);
						// oos.writeObject(cmd);
						oos.writeObject(handleSignUp(scanner));
						break;
					case "로그인":
						User user = handleLogin(scanner);
						userId = UserDAO.loginUser(user);
						oos.writeObject(user);
						if (userId == 0) {
							isLoggedIn = false;
						} else {
							isLoggedIn = true;
						}
						break;
					case "로그아웃":
						System.out.println("프로그램 종료");
						return;
					default:
						System.out.println("1~3 사이의 값을 입력해주세요.");
						break;
					}
				} else {
					// 로그인한 경우
					oos.reset();
					oos.flush();
					displayMainMenu(scanner);
					int mainMenuChoice = scanner.nextInt();

					switch (mainMenuChoice) {
					case ServiceMenu2.투두리스트_작성:
						oos.writeObject(handleToDoListCreation(scanner));
						break;
					case ServiceMenu2.투두리스트_삭제:
						Optional<ToDoList> optionalToDoList = handleToDoListDelete(scanner);
						optionalToDoList.ifPresentOrElse(toDoList -> {
							try {
								oos.writeObject(toDoList);
								System.out.println("투두리스트 삭제가 완료되었습니다.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, () -> System.out.println("투두리스트 삭제가 취소되었습니다."));
					case ServiceMenu2.투두리스트_수정:

						break;
					case ServiceMenu2.투두리스트_전체_조회:
						oos.writeObject(handleToDoListAll()); 
						break;
					case ServiceMenu2.투두리스트_완료:
						oos.writeObject(handleToDoListCom()); 
						break;
					case ServiceMenu2.투두리스트_미완료:
						oos.writeObject(handleToDoListIncom()); 
						break;
					case ServiceMenu2.다이어리_작성:
						oos.writeObject(handleDiaryEntry(scanner));
						break;
					case ServiceMenu2.다이어리_삭제:
//						handleDiaryDeletion(scanner);
						break;
					case ServiceMenu2.다이어리_수정:
						break;
					case ServiceMenu2.다이어리_전체_조회:
						oos.writeObject(handleDiaryAllRetrieval());
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
		System.out.println("회원가입");
		System.out.println("로그인");
		System.out.println("로그아웃");
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
		User user = new User("회원가입", username, password);

		return user;
	}

	private static User handleLogin(Scanner scanner) throws IOException {
		// 로그인 처리
		System.out.println("----------로그인----------");
		System.out.print("ID를 입력해주세요: ");
		String username = scanner.nextLine();
		System.out.print("비밀번호를 입력해주세요: ");
		String password = scanner.nextLine();

		// 회원가입 정보를 서버로 전송
		User user = new User("로그인", username, password);
		return user;
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

	private static ToDoList handleToDoListCreation(Scanner scanner) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("----------투두리스트 작성----------");
		System.out.print("제목을 입력해주세요: ");
		String title = br.readLine();
		System.out.print("할 일을 입력해주세요: ");
		String content = br.readLine();
		System.out.print("마감일을 입력해주세요(예: 2024-02-19): ");
		String closedDate = br.readLine();
		System.out.print("우선순위를 숫자로 입력해주세요: ");
		Integer priority = Integer.parseInt(br.readLine());
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_작성, userId, title, content, closedDate, priority);

		return toDoList;
	}

	private static Optional<ToDoList> handleToDoListDelete(Scanner scanner) throws IOException {
		System.out.println("----------투두리스트 삭제----------");
		System.out.print("삭제하실 투두리스트의 제목을 입력해주세요: ");
		String title = scanner.next();
		System.out.print("정말 삭제하시겠습니까?(Y/N) ");
		String ack = scanner.next();
		switch (ack) {
		case "Y", "y":
			ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_삭제, userId, title, null, null, null);
			return Optional.of(toDoList);
		case "N", "n":
			return Optional.empty();
		default:
			System.out.println("잘못된 입력입니다.");
			return Optional.empty();
		}
	}

	private static ToDoList handleToDoListAll() throws IOException {
		System.out.println("----------투두리스트 전체 조회----------");
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_전체_조회, userId);
		try {
			ToDoListDAO.getTodoList("all", toDoList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}
	
	private static ToDoList handleToDoListCom() throws IOException {
		System.out.println("----------완료된 투두리스트 조회----------");
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_완료, userId);
		try {
			ToDoListDAO.getTodoList("completed", toDoList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}
	
	private static ToDoList handleToDoListIncom() throws IOException {
		System.out.println("----------미완료된 투두리스트 조회----------");
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_미완료, userId);
		try {
			ToDoListDAO.getTodoList("incomplete", toDoList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}

	private static Diary handleDiaryEntry(Scanner scanner) throws IOException {
		// 일기 작성 처리
		System.out.println("----------일기 작성----------");
		System.out.print("제목을 입력해주세요: ");
		String title = scanner.next();
		scanner.nextLine();
		System.out.print("내용을 입력해주세요: ");
		String content = scanner.next();
		scanner.nextLine();

		// 서버로부터 결과 수신
		Diary diary = new Diary(ServiceMenu2.다이어리_작성, title, content, userId);
		System.out.println(diary.toString());

		return diary;
	}
	
	private static Diary handleDiaryAllRetrieval() throws IOException {
		// 다이어리 전체 조회 처리
		System.out.println("----------전체 일기 조회----------");
		return new Diary(ServiceMenu2.다이어리_전체_조회, userId);
	}

	private static Diary handleDiarySpecRetrieval(Scanner scanner, String str) throws IOException {
		// 다이어리 특정 날짜 조회 처리
		System.out.println("----------특정 날짜 일기 조회----------");
		System.out.println("조회를 원하는 날짜를 입력하세요. [2000-01-01] 포맷으로 입력하세요.");
		LocalDate createDate = LocalDate.parse(scanner.next(), DateTimeFormatter.ISO_DATE);

		return new Diary(ServiceMenu2.다이어리_특정날짜, userId, createDate);

	}
//
//	private static void handleDiaryRetrieval(Scanner scanner)
//			throws IOException {
//		// 일기 조회 처리
//		writer.println("----------일기 조회----------");
//		// 서버로부터 결과 수신 및 출력
//	}
//
//	private static void handleDiaryDeletion(Scanner scanner)
//			throws IOException {
//		// 일기 삭제 처리
//		writer.println("----------일기 삭제----------");
//		// 서버로부터 결과 수신 및 출력
//	}
}