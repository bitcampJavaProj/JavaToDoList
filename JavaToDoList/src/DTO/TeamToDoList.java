package DTO;

import java.sql.Date;
import java.time.LocalDate;

public class TeamToDoList {
	private Integer teamId;
	private String title;
	private String content;
	private LocalDate createDate;
	private String closedDate;
	private Integer priority;
	private Boolean isComplete;
	private Boolean isDeleted;
		
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Boolean getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Override
	public String toString() {
		String str = String.format("할 일: %s\n메모: %s\n생성 날짜: %s\n마감 날짜: %s\n우선 순위: %d\n완료 여부: %d\n삭제 여부: %d\n", 
									title, content, createDate, closedDate, priority, isComplete, isDeleted);
		return str;
	}
}
