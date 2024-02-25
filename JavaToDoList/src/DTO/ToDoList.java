package DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class ToDoList extends Cmd implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer toDoListId;
	private Integer userId;
	private String title;
	private String newTitle;
	private String content;
	private LocalDate createDate;
	private String closedDate;
	private Integer priority;
	private Boolean isComplete;
	private Boolean isDeleted;

	// getTodoList에서 사용
	public ToDoList(Integer toDoListId, Integer userId, String title, String content, LocalDate createDate,
			String closedDate, Integer priority, Boolean isComplete, Boolean isDeleted) {
		this.toDoListId = toDoListId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.closedDate = closedDate;
		this.priority = priority;
		this.isComplete = isComplete;
		this.isDeleted = isDeleted;
	}
	
	// handleToDoListCreation에서 사용
	public ToDoList(Integer cmd, Integer userId, String title, String content, String closedDate, Integer priority,
			Boolean isComplete) {
		this.cmd = cmd;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.closedDate = closedDate;
		this.priority = priority;
		this.isComplete = isComplete;
	}

	// handleToDoListDelete, handleToDoListUpdate에서 사용
	public ToDoList(Integer cmd, Integer userId, String title) {
		this.cmd = cmd;
		this.userId = userId;
		this.title = title;
	}
	
	// handleToDoListAll/Com/Incom에서 사용
	public ToDoList(Integer cmd, Integer userId) {
		this.cmd = cmd;
		this.userId = userId;
	}

	public Integer getToDoListId() {
		return userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNewTitle() {
		return newTitle;
	}

	public void setNewTitle(String newTitle) {
		this.newTitle = newTitle;
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
		String str = String.format("할 일: %s\n메모: %s\n생성 날짜: %s\n마감 날짜: %s\n우선 순위: %d\n완료 여부: %b\n", title, content,
				createDate, closedDate, priority, isComplete);
		return str;
	}
}
