package DTO;

import java.io.Serializable;

/**
 * @author 서혜리
 * 
 * Cmd 클래스
 * : 명령어 관련 클래스
 */
public class Cmd implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer cmd;
	
	public Integer getCmd() {
		return cmd;
	}
	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}
}
