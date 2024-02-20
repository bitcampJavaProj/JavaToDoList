package DAO;

/**
 * TodoList
 * : Todo List의 각 list들
 * 
 * @author orbit
 */
public class teamTodoList {
	// field
	private Integer teamId;						// 고유 ID
	private String title;						// 제목
	private String content;						// 내용
	private java.util.Date createDate;			// 생성일
	private java.util.Date closedDate;			// 마감일
	private Integer priority;					// 우선순위
	private Boolean isComplete;					// 완료 여부 (0 = false(default), 1 = true)
	private Boolean isDeleted;					// 삭제 여부 (0 = false, 1 = true)
	
	public teamTodoList (
			Integer teamId, 
			String title, 
			String content, 
			java.util.Date createDate, 
			java.util.Date closedDate,
			Integer priority,
			Boolean isComplete,
			Boolean isDeleted
			) {
		this.teamId = teamId;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.closedDate = closedDate;
		this.priority = priority;
		this.isComplete = isComplete;
		this.isDeleted = isDeleted;	
	}
	
	// getter & setter
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

	public java.util.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public java.util.Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(java.util.Date closedDate) {
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
	
}