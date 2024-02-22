package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import org.json.JSONObject;

import DAO.UserDAO;
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
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
//			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			while(true) {
				/*client가 json오브젝트를 string으로 변환해서 보낸 것을 수신*/
				String line = br.readLine();
				//System.out.println("raw data=" + line);
				if(line == null)
					break;
				/*json패킷을 해석해서 알맞은 처리를 한다.
				 * 문자열 -> JSONObject 변환 -> cmd를 해석해서 어떤 명령인지?
				 * */
				JSONObject packetObj = new JSONObject(line);
				// 명령(cmd)당 알맞은 처리를 해줌
				processPacket(packetObj);
				
			}
		} catch (Exception e) {
			System.out.printf("<서버-%s>%s\n", getName(), e.getMessage());
		}
	}
	
	private void processPacket(JSONObject packetObj) throws IOException {
		// 클라이언트에 응답을 하기 위한 json 오브젝트
		JSONObject ackObj = new JSONObject();
		// 어떤 종류의 패킷을 보냈는지 분류하기 위한 정보
		String cmd = packetObj.getString("cmd");
		
		if(cmd.equals("join")) {
			try {
				UserDAO.insertUser("idididid", "pwpww");  //example!!
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(cmd.equals("login")) {
			
			try {
				UserDAO.loginUser("ididididid", "pwpwpwpwpw");  //example!!
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 여기에서 기능이 구현되지 않을까....
		}  else if(cmd.equals("QUIT")) {
			String id = packetObj.getString("id");
			
			System.out.printf("<서버-%s> Id=%s QUIT 요청 \n", getName(), id);
			
			// 관리에서 제외
			ht.remove(id);
			
			ackObj.put("cmd", "QUIT");
			ackObj.put("id", id);
			String ack = ackObj.toString();
			// 클라이언트한테 전송
			OutputStream out = this.socket.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			pw.println(ack);
			pw.flush();
		}
	}
}





















