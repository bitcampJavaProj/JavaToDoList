package DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class Diary extends Cmd implements Serializable {
   private static final long serialVersionUID = 1L;
   private Integer diaryId;
   private String title;
   private String content;
   private Integer userId;
   private LocalDate createDate;
   private Boolean isDeleted;

   public Diary() {}

   public Diary(Object cmd, String title, String content, Integer userId) {
	   this.cmd = cmd;
	   this.title = title;
	   this.content = content;
	   this.userId = userId;
   }

   public Diary(Integer diaryId, String title, String content, Integer userId, LocalDate createDate,
         Boolean isDeleted) {
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

   public LocalDate getCreateDate() {
      return createDate;
   }

   public void setCreateDate(LocalDate createDate) {
      this.createDate = createDate;
   }
   
   public Boolean getIsDeleted() {
      return isDeleted;
   }

   public void setIsDeleted(Boolean isDeleted) {
      this.isDeleted = isDeleted;
   }

   @Override
   public String toString() {
      String str = String.format("제목: %s\n내용: %s\n작성자id: %d\n생성 날짜: %s\n", title, content, userId, createDate);

      return str;
   }
}