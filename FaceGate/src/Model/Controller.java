package Model;
import java.sql.*;
import java.util.*;


public class Controller {
   public static int check;
   public static int select;
   public static Scanner sc=new Scanner(System.in);
   
   //�α���
   public static void LogIn(String id,String pw) {
      check=0;
      DbManager db=new DbManager();
      while(check==0) {
         check=db.LogIn(id,pw);
         }
   }
   
   //ȸ������
   public static void SignUp(String id,String pw,int index) {
      check=0;
      DbManager db=new DbManager();
      while(check!=1) {
         check=db.managerSignUp(id,pw,index);
      }
   }
   
   //�������
   public static void Delete(int Employee_IDX) {
      DbManager db=new DbManager();
      db.deleteEmployee(Employee_IDX);
   }
   
   //����߰�
   public static void AddEmployee(int Employee_IDX, String Employee_NM, String Employee_DP, String Employee_CP) {
      check=0;
      while(check!=1) {
         DbManager db=new DbManager();
         check=db.employeeSignUp(Employee_IDX,Employee_NM, Employee_DP,Employee_CP);
         }
   }
   
}