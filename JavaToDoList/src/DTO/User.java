package DTO;

import java.io.Serializable;

public class User extends Cmd implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String username;
	private String password;
	
	public User() {}
	public User(Integer cmd, String username, String password) {
		this.cmd = cmd;
		this.username = username;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		String str = String.format("아이디:%s \n이름:%s \n비밀번호:%s ", userId, username, password);
		return str;
	}
}