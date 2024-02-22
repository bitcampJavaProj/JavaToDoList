package server;

import DAO.*;
import mysql.DBConnection;

public class Server {
    public static void main(String[] args) {
        // DB 연결 초기화
        DBConnection.openConnection();
        
        DBConnection.closeConnection();
    }
}