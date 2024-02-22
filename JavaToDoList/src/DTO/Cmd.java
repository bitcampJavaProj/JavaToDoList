package DTO;

import java.io.Serializable;

/**
 * @author 서혜리
 * 
 * Cmd 클래스
 * : 명령어 관련 클래스
 * @param <T>
 */
public class Cmd implements Serializable {
	private static final long serialVersionUID = 1L;
	Object cmd;
	
	public Cmd() {}
	public Cmd(Object cmd) {
		this.cmd = cmd;
	}
	
	public Object getCmd() {
		return cmd;
	}
	public void setCmd(Object cmd) {
		this.cmd = cmd;
	}
}
