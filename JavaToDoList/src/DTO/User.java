package DTO;

public class User {

   private Integer userId;
   private String username;
   private String password;
   
   
   public Integer getuserId() {
      return userId;
   }

   public void setId(Integer userId) {
      this.userId = userId;
   }
   
   public String getusername() {
      return username;
   }

   public void setusername(String username) {
      this.username = username;
   }
   
   public String getpassword() {
      return password;
   }
   
   public void setpassword(String password) {
      this.password = password;
   }   

   @Override
   public String toString() {
      String str = String.format("아이디:%s \n이름:%s \n비밀번호:%s ",
            userId, username, password );
      return str;
   }   
}