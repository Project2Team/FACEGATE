package Model;
import java.sql.*;
import java.util.*;


public class FaceMain {
	public static Calendar cal = Calendar.getInstance();
	public static Connection conn=null;
	public static Statement stmt=null;
	public static ResultSet rs=null;
	public static PreparedStatement pstmt=null;
	public static ArrayList<String> ID=new ArrayList<String>();
	public static ArrayList<String> PW=new ArrayList<String>(); 
	public static ArrayList<Integer> Num=new ArrayList<Integer>();
	public static String id;
	public static String pw;
	public static int num;
	public static int index;
	DbManager db=new DbManager();

	public static Scanner sc=new Scanner(System.in);
	public static void main(String[] args) {
		DbManager db=new DbManager();
		System.out.println("1.�α��� 2.ȸ������");
		int select=sc.nextInt();
		int check1=1;
		int check2=0;
		if(select==1) { //�α���
			
			while(check1==0) {
				System.out.println("���̵� �Է�");
				id=sc.next();
				System.out.println("��й�ȣ �Է�");
				pw=sc.next();
				check1=db.LogIn(id,pw);}
		}else if(select==2) { //ȸ������
			while(check2!=1) {
				System.out.println("���̵� �Է�");
				id=sc.next();
				System.out.println("��й�ȣ �Է�");
				pw=sc.next();
				System.out.println("�����ȣ �Է�");
				index=sc.nextInt();
				check2=db.managerSignUp(id,pw,index);
				
			}
		}
		while(true) {
			
		System.out.println("1.��� 2.��� 3.����");
		select=sc.nextInt();
		Controller cont =new Controller();
		
		switch(select) {
		case 1:cont.getImage(1);
		break;
		case 2:cont.getImage(2);
		break;
		case 3:RunManage();
		break;
		}
		}
	}
		
	
	
	public static void RunManage() { //������ �޴�
		DbManager db=new DbManager();
		System.out.println("1.������� 2.����߰�");
		int select=sc.nextInt();
		System.out.println("�����ȣ  �Է�");
		int Employee_IDX=sc.nextInt();
		int check3=0;
		switch(select) {
		case 1:
			db.deleteEmployee(Employee_IDX);
		case 2:
			while(check3!=1) {
			System.out.println("�̸�  �Է�");
			String Employee_NM=sc.next();
			System.out.println("�μ�  �Է�");
			String Employee_DP=sc.next();
			String Employee_CP=null; //��ǻ�Ͱ��
			check3=db.employeeSignUp(Employee_IDX,Employee_NM, Employee_DP,Employee_CP);
		
			}
		}
	}
	
	
	
	
	

}
