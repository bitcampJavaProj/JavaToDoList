package DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class Diary extends Cmd implements Serializable {
   private static final long serialVersionUID = 1L;
   private Integer diaryId;
   private String title;
   private String content;
   private Integer userId;
   private String createDate;
   private Boolean isDeleted;
   
   private String newTitle;
   
  






public Diary(Object cmd, String content, Integer userId, String newTitle) {
	this.cmd = cmd;
	this.content = content;
	this.userId = userId;
	this.newTitle = newTitle;
}







//
//public Diary(Object cmd, String newTitle, String content, Integer userId) {
//	this.cmd = cmd;
//	this.content = content;
//	this.userId = userId;
//	this.newTitle = newTitle;
//}
//







public String getNewTitle() {
	return newTitle;
}




public void setNewTitle(String newTitle) {
	this.newTitle = newTitle;
}




public Diary() {}



   
public Diary(Object cmd, Integer userId, String createDate) {
	this.cmd = cmd;
	this.userId = userId;
	this.createDate = createDate;
}




public Diary(Object cmd, Integer userId) {
	this.cmd = cmd;
	this.userId = userId;
}



public Diary(Object cmd, String title, Integer userId) {
	this.cmd = cmd;
	this.title = title;
	this.userId = userId;
}

public Diary(Object cmd, String title, String content, Integer userId) {
	   this.cmd = cmd;
	   this.title = title;
	   this.content = content;
	   this.userId = userId;
   }

 
   
   

   
   
public Diary(Integer diaryId, String title, String content, Integer userId, String createDate, Boolean isDeleted) {
	this.diaryId = diaryId;
	this.title = title;
	this.content = content;
	this.userId = userId;
	this.createDate = createDate;
	this.isDeleted = isDeleted;
}


   public Integer getDiaryId() {
	return diaryId;
}




public void setDiaryId(Integer diaryId) {
	this.diaryId = diaryId;
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




public Integer getUserId() {
	return userId;
}




public void setUserId(Integer userId) {
	this.userId = userId;
}




public String getCreateDate() {
	return createDate;
}




public void setCreateDate(String createDate) {
	this.createDate = createDate;
}




public Boolean getIsDeleted() {
	return isDeleted;
}




public void setIsDeleted(Boolean isDeleted) {
	this.isDeleted = isDeleted;
}




public static long getSerialversionuid() {
	return serialVersionUID;
}




@Override
   public String toString() {
      String str = String.format("제목: %s\n내용: %s\n작성자id: %d\n생성 날짜: %s\n", title, content, userId, createDate);

      return str;
   }
}