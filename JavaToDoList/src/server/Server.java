package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import DAO.ToDoListDAO;
import DAO.UserDAO;
import DTO.Cmd;
import DTO.Diary;
import DTO.ToDoList;
import DTO.User;
import client.ServiceMenu;
import client.ServiceMenu2;
import mysql.DBConnection;

public class Server {
    public static void main(String[] args) {
        // DB 연결 초기화
        DBConnection.openConnection();
        
		final int PORT = 9000;
		Hashtable<String, Socket> clientHt = new Hashtable<>(); // 접속자를 관리하는 테이블

		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			String mainThreadName = Thread.currentThread().getName();
			/* main thread는 계속 accept()처리를 담당한다 */
			while (true) {
				System.out.printf("[서버-%s] Client접속을 기다립니다...\n", mainThreadName);
				Socket socket = serverSocket.accept();

				/* worker thread는 Client와의 통신을 담당한다. */
				WorkerThread wt = new WorkerThread(socket, clientHt);
				wt.start();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        DBConnection.closeConnection();
    }
}


class WorkerThread extends Thread {
	private Socket socket;
	private Hashtable<String, Socket> ht;
	private Cmd cmdObj;
	private ToDoList toDoListObj;
	private Diary diaryObj;

	public WorkerThread(Socket socket, Hashtable<String, Socket> ht) {
		this.socket = socket;
		this.ht = ht;
	}

	@Override
	public void run() {
		try {
			InetAddress inetAddr = socket.getInetAddress();
			System.out.printf("<서버-%s>%s로부터 접속했습니다.\n", getName(), inetAddr.getHostAddress());
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						
			while(true) {
				/**
				 * @author 서헤리
				 * 
				 * ObjectInputStream인 ois를 사용하여
				 * 클라이언트로부터 전송된 객체를 수신함
				 * 수신된 객체가 null일 경우 반복문 종료 + 스레드 실행 종료
				 */
				Object packetObj = ois.readObject();
				if(packetObj == null)
					break;
				processPacket(packetObj);
			}
		} catch (Exception e) {
			System.out.printf("<서버-%s>%s\n", getName(), e.getMessage());
		}
	}
	
	private void processPacket(Object packetObj) throws Exception {
		// 클라이언트에 응답을 하기 위한 오브젝트
		Object ackObj = new Object();
		
		cmdObj = (Cmd)packetObj;
		if(cmdObj.getCmd() == ServiceMenu.회원가입) {
			try {
				if (cmdObj instanceof User) {
					User userObj = (User)cmdObj;
					UserDAO.insertUser(userObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(cmdObj.getCmd() == ServiceMenu.로그인) {
			try {
				User userObj = (User)cmdObj;
				UserDAO.loginUser(userObj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(cmdObj instanceof ToDoList) {
				toDoListObj = (ToDoList)cmdObj;
			} else if(cmdObj instanceof Diary) {
				diaryObj = (Diary)cmdObj;
			}

			switch (cmdObj.getCmd()) {
			case ServiceMenu2.투두리스트_작성: 
				ToDoListDAO.insertToDoList(DBConnection.getConnection(), toDoListObj);
				break;
			case ServiceMenu2.투두리스트_삭제: 
				ToDoListDAO.deleteToDoList(DBConnection.getConnection(), toDoListObj);
				break;
			case ServiceMenu2.투두리스트_수정: 
				ToDoListDAO.updateToDoList(DBConnection.getConnection(), toDoListObj);
				break;
			case ServiceMenu2.투두리스트_전체_조회: 
				
				break;
			case ServiceMenu2.투두리스트_완료: 
				
				break;
			case ServiceMenu2.투두리스트_미완료: 
				
				break;
			case ServiceMenu2.다이어리_작성: 
				
				break;
			case ServiceMenu2.다이어리_삭제: 
				
				break;
			case ServiceMenu2.다이어리_수정: 
				
				break;
			case ServiceMenu2.다이어리_전체_조회: 
				
				break;
			case ServiceMenu2.다이어리_특정날짜: 
				
				break;
			}
		}  else if(cmdObj.getCmd() == ServiceMenu.로그아웃) {
//			String id = packetObj.getString("id");
			
//			System.out.printf("<서버-%s> Id=%s 로그아웃 요청 \n", getName(), id);
			
			// 관리에서 제외
//			ht.remove(id);
//			
//			ackObj.put("cmd", "QUIT");
//			ackObj.put("id", id);
			String ack = ackObj.toString();
			// 클라이언트한테 전송
			OutputStream out = this.socket.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			pw.println(ack);
			pw.flush();
		}
	}
}