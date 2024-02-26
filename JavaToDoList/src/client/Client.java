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
		Socket socket = new Socket(SERVER_IP, SERVER_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Scanner scanner = new Scanner(System.in);
		try {
			boolean isLoggedIn = false;

			exitWhile:
			while (true) {
				if (!isLoggedIn) {
					displayLoginMenu(scanner);
					String loginChoice = scanner.nextLine();

					switch (loginChoice) {
					case "회원가입":
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
					default:
						System.out.println("회원가입/로그인 중 하나를 입력해주세요.");
						break;
					}
				} else {
					oos.reset();
					oos.flush();
					displayMainMenu(scanner);
					int mainMenuChoice = scanner.nextInt();

					switch (mainMenuChoice) {
					case ServiceMenu2.투두리스트_작성:
						oos.writeObject(handleToDoListCreation(scanner));
						System.out.println("투두리스트 작성이 완료되었습니다.");
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
						Optional<ToDoList> optionalToDoList2 = handleToDoListUpdate(scanner);
						optionalToDoList2.ifPresentOrElse(toDoList -> {
							try {
								oos.writeObject(toDoList);
								System.out.println("투두리스트 수정이 완료되었습니다.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, () -> System.out.println("투두리스트 수정이 취소되었습니다."));
					case ServiceMenu2.투두리스트_전체_조회:
						oos.writeObject(handleToDoList("all"));
						break;
					case ServiceMenu2.투두리스트_완료:
						oos.writeObject(handleToDoList("completed"));
						break;
					case ServiceMenu2.투두리스트_미완료:
						oos.writeObject(handleToDoList("incomplete"));
						break;
					case ServiceMenu2.다이어리_작성:
						oos.writeObject(handleDiaryEntry(scanner));
						break;
					case ServiceMenu2.다이어리_삭제:
						Optional<Diary> optionalDiary = handleDiaryDelete(scanner);
						optionalDiary.ifPresentOrElse(diary -> {
							try {
								oos.writeObject(diary);
								System.out.println("다이어리 삭제가 완료되었습니다.");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}, () -> System.out.println("다이어리 삭제가 취소되었습니다."));
						break;
					case ServiceMenu2.다이어리_수정:
						break;
					case ServiceMenu2.다이어리_전체_조회:
						oos.writeObject(handleDiaryAllRetrieval());
						break;
					case ServiceMenu2.다이어리_특정날짜:
						break;
					case ServiceMenu2.로그아웃:
						userId = 0;
						isLoggedIn = false;
						System.out.println("--------------------");
						System.out.println("로그아웃 되었습니다.");
						break exitWhile;
					default:
						System.out.println("1~12 사이의 값을 입력해주세요.");
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 회원 관련 메뉴
	private static void displayLoginMenu(Scanner scanner) {
		System.out.println("----------옵션 선택----------");
		System.out.println("회원가입");
		System.out.println("로그인");
		System.out.println();
	}

	/**
	 * @author 유민진/서혜리<br>
	 *         handleSignUp : 회원가입. 사용자가 입력한 유저 정보를 반환하는 기능<br>
	 * 
	 * @return user : 회원가입에 성공하면 user 객체를 반환
	 */
	private static User handleSignUp(Scanner scanner) throws IOException {
		System.out.println("----------회원가입----------");
		System.out.print("사용하실 ID를 입력해주세요: ");
		String username = scanner.nextLine();
		System.out.print("비밀번호를 입력해주세요: ");
		String password = scanner.nextLine();

		// 회원가입 정보를 서버로 전송
		User user = new User("회원가입", username, password);

		return user;
	}

	/**
	 * @author 유민진/박지우<br>
	 *         handleLogin : 로그인. 사용자가 입력한 유저 정보를 반환하는 기능<br>
	 * 
	 * @return user : 로그인에 성공하면 user 객체를 반환
	 */
	private static User handleLogin(Scanner scanner) throws IOException {
		System.out.println("----------로그인----------");
		System.out.print("ID를 입력해주세요: ");
		String username = scanner.nextLine();
		System.out.print("비밀번호를 입력해주세요: ");
		String password = scanner.nextLine();

		User user = new User("로그인", username, password);
		return user;
	}

	// 주 메뉴
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
		System.out.println("12. 로그아웃");
		System.out.println();
	}

	/**
	 * @author 서혜리<br>
	 *         handleToDoListCreation : 투두리스트 작성. 사용자가 입력한 투두리스트 정보를 반환하는 기능<br>
	 * 
	 * @return toDoList : 투두리스트 작성에 성공하면 toDoList 객체를 반환함
	 */
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
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_작성, userId, title, content, closedDate, priority, false);

		return toDoList;
	}

	/**
	 * @author 서혜리<br>
	 *         handleToDoListDelete : 투두리스트 삭제.<br>
	 *         사용자가 입력한 투두리스트 제목과 일치하는 제목을 가진 투두리스트가 존재한다면 그 투두리스트를 반환하는 기능<br>
	 * 
	 * @return toDoList : 투두리스트 작성에 성공하면 toDoList 객체를 반환함
	 */
	private static Optional<ToDoList> handleToDoListDelete(Scanner scanner) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("----------투두리스트 삭제----------");
		System.out.print("삭제하실 투두리스트의 제목을 입력해주세요: ");
		String title = br.readLine();
		System.out.print("정말 삭제하시겠습니까?(Y/N) ");
		String ack = br.readLine();
		switch (ack) {
		case "Y", "y":
			ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_삭제, userId, title);
			return Optional.of(toDoList);
		case "N", "n":
			return Optional.empty();
		default:
			System.out.println("잘못된 입력입니다.");
			return Optional.empty();
		}
	}

	/**
	 * @author 서혜리<br>
	 *         handleToDoListUpdate : 투두리스트 수정.<br>
	 *         사용자가 입력한 투두리스트 제목과 일치하는 제목을 가진 투두리스트가 존재한다면 값을 수정해 반환하는 기능<br>
	 * 
	 * @return toDoList : 투두리스트 작성에 성공하면 toDoList 객체를 반환함
	 */
	private static Optional<ToDoList> handleToDoListUpdate(Scanner scanner) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("----------투두리스트 수정----------");
		System.out.print("수정하실 투두리스트의 제목을 입력해주세요: ");
		String title = br.readLine();
		ToDoList toDoList = new ToDoList(ServiceMenu2.투두리스트_수정, userId, title);
		System.out.print("수정하시겠습니까?(Y/N) ");
		String ack = br.readLine();
		switch (ack) {
		case "Y", "y":
			System.out.print("수정할 제목을 입력해주세요: ");
			String updateTitle = br.readLine();
			System.out.print("수정할 할 일을 입력해주세요: ");
			String updateContent = br.readLine();
			System.out.print("수정할 마감일을 입력해주세요(예: 2024-02-19): ");
			String updateClosedDate = br.readLine();
			System.out.print("우선순위를 입력해주세요: ");
			Integer updatePriority = Integer.parseInt(br.readLine());
			System.out.print("완료 여부를 수정해주세요(true/false): ");
			Boolean updateIsComplete = Boolean.parseBoolean(br.readLine());
			toDoList.setNewTitle(updateTitle);
			toDoList.setContent(updateContent);
			toDoList.setClosedDate(updateClosedDate);
			toDoList.setPriority(updatePriority);
			toDoList.setIsComplete(updateIsComplete);
			return Optional.of(toDoList);
		case "N", "n":
			return Optional.empty();
		default:
			System.out.println("잘못된 입력입니다.");
			return Optional.empty();
		}
	}

	/**
	 * @author 박지우/서혜리<br>
	 *         handleToDoList : 투두리스트 조회<br>
	 * 
	 * @param filter : 해당하는 filter 값 조회 all/completed/incomplete
	 * @return toDoList : 투두리스트 작성에 성공하면 toDoList 객체를 반환함
	 */
	private static ToDoList handleToDoList(String filter) throws IOException {
		System.out.printf("----------투두리스트 %s 조회----------",
				filter == "all" ? "전체" : filter == "completed" ? "완료 목록" : "미완료 목록");
		ToDoList toDoList = new ToDoList(filter == "all" ? ServiceMenu2.투두리스트_전체_조회
				: filter == "completed" ? ServiceMenu2.투두리스트_완료 : ServiceMenu2.투두리스트_미완료, userId);

		try {
			ToDoListDAO.getTodoList(filter, toDoList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toDoList;
	}

	/**
	 * @author 권재원<br>
	 *         handleDiaryEntry : 일기 작성<br>
	 * 
	 * @return diary :
	 */
	private static Diary handleDiaryEntry(Scanner scanner) throws IOException {
		System.out.println("----------일기 작성----------");
		System.out.print("제목을 입력해주세요: ");
		String title = scanner.next();
		scanner.nextLine();
		System.out.print("내용을 입력해주세요: ");
		String content = scanner.next();
		scanner.nextLine();

		Diary diary = new Diary(ServiceMenu2.다이어리_작성, title, content, userId);
		System.out.println(diary.toString());

		return diary;
	}

	/**
	 * @author 김동우<br>
	 *         handleDiaryAllRetrieval : 일기 전체 조회<br>
	 * 
	 * @return diary :
	 */
	private static Diary handleDiaryAllRetrieval() throws IOException {
		System.out.println("----------전체 일기 조회----------");
		return new Diary(ServiceMenu2.다이어리_전체_조회, userId);
	}

	/**
	 * @author 김동우<br>
	 *         handleDiarySpecRetrieval : 일기 전체 조회<br>
	 * 
	 * @return diary :
	 */
	private static Diary handleDiarySpecRetrieval(Scanner scanner, String str) throws IOException {
		System.out.println("----------특정 날짜 일기 조회----------");
		System.out.println("조회를 원하는 날짜를 입력하세요. [2000-01-01] 포맷으로 입력하세요.");
		LocalDate createDate = LocalDate.parse(scanner.next(), DateTimeFormatter.ISO_DATE);

		return new Diary(ServiceMenu2.다이어리_특정날짜, userId, createDate);

	}

	/**
	 * * @author 권재원<br>
	 * * handleDiaryEntry : 제목을 입력받아 일기 삭제 and null처리를 위해 Optional 사용<br>
	 * * * @return diary :
	 */
	private static Optional<Diary> handleDiaryDelete(Scanner scanner) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("----------일기 삭제----------");
		System.out.print("삭제하실 일기의 제목을 입력해주세요: ");
		String title = br.readLine();
		System.out.print("정말 삭제하시겠습니까?(Y/N) ");
		String ack = br.readLine();
		switch (ack) {
		case "Y", "y":
			Diary diary = new Diary(ServiceMenu2.다이어리_삭제, userId, title);
			return Optional.of(diary);
		case "N", "n":
			return Optional.empty();
		default:
			System.out.println("잘못된 입력입니다.");
			return Optional.empty();
		}
	}
}