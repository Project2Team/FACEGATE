package Model;

import java.sql.*;
import java.util.ArrayList;

public class DbManager {
	public static void main(String[] args) {
		new DbManager();
	}

	Connection cnt = null;
	Statement stat = null;

	public DbManager() {
		try {
			// Class Load
			Class.forName("org.sqlite.JDBC");
			System.out.println("Find org.sqlite.JDBC");

			try {
				// create a database connection
				cnt = DriverManager.getConnection("jdbc:sqlite:FaceGate.db");
				stat = cnt.createStatement();

				ResultSet rs = stat.executeQuery("select *from EMPLOYEE");
				while (rs.next()) {
					System.out.println(rs.getInt("Employee_IDX"));
				}

			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println(e.getMessage());
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Can't find org.sqlite.JDBC");
			System.err.println(e.getMessage());
		}

		//�Լ� ���� �����Դϴ�. 
		Commute_On(201811259);
		Commute_Off(201811259);
		managerSignUp("baesubin18", "qotnqls", 201811259);
		employeeSignUp(201811259, "�����", "DB/��������", null);
		LogIn("baesubin18", "qotnqls");
		deleteEmployee(201811259);
		getEmployeeData("�����");
	}

	public int Commute_On(int Employee_IDX) {
		// return 1 : ��� �Է� ����
		// return 2 : ����� �ƴ� -> �Է¾���

		try {
			// Employee_IDX�� Employee���̺� ������ ���,
			// �ش� Employee_IDX�� ���� ����ð� insert
			ResultSet rs = stat.executeQuery(
					"select EXISTS (select * from EMPLOYEE where Employee_IDX = " + Employee_IDX + ") as success");
			int flag = rs.getInt(1);
			if (flag != 0) {// ���O
				String query = "insert into COMMUTE ( Employee_IDX, Commute_ON_TM, Commute_OFF_TM ) " + "values ("
						+ Integer.toString(Employee_IDX) + ",datetime('now', 'localtime'), NULL)";
				if (stat.executeUpdate(query) >= 1) {
					System.out.println(Employee_IDX + "�� ��� �Ϸ�");
					return 1;
				} else
					return 0;
			} else {// ���X
				System.out.println("����� �ƴմϴ�. ");
				return 2;
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return 0;
	}

	public int Commute_Off(int Employee_IDX) {
		// return 1: ����Է� ����
		try {
			// Commute���̺� ��ٻ����� Employee�� ���ؼ�
			// Commute_Off�÷��� ����ð� update
			String query = "update Commute set Commute_OFF_TM = datetime('now', 'localtime') "
					+ "where Commute_OFF_TM is NULL And COMMUTE.Employee_IDX =" + Employee_IDX;
			if (stat.executeUpdate(query) >= 1) {
				System.out.println(Employee_IDX + "�� ��� �Ϸ�");
				return 1;
			} else {
				System.out.println("�������� ���� ����̰ų� ��ٻ��°� �ƴմϴ�. ");
				return 0;
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return 0;
	}

	public int managerSignUp(String Manager_Id, String Manager_Pwd, Integer Employee_IDX) {
		// return 0 : �Է� ����
		// return 1 : �Է� ����
		// return 2 : ����� �ƴ� -> �Է¾���
		// return 3 : �̹� �����ڷ� ��ϵ� ��� -> �Է¾���

		ResultSet rs;

		try {
			rs = stat.executeQuery(
					"select EXISTS (select * from EMPLOYEE where Employee_IDX = " + Employee_IDX + " ) as success");

			int flag = rs.getInt(1);
			if (flag != 0) { // ���O Ȯ��
				rs = stat.executeQuery(
						"select EXISTS (select * from Manager where Employee_IDX = " + Employee_IDX + " ) as success");
				if (rs.getInt(1) == 1) { // ���O, ������O
					System.out.println("�̹� �����ڷ� ��ϵ� ����Դϴ�. ");
					return 3;
				} else { // ���O , ������X ���� -> DB�� �Է���
					String query = "insert into MANAGER ( Employee_IDX, Manager_ID, Manager_PWD ) " + "values ("
							+ Integer.toString(Employee_IDX) + ", '" + Manager_Id + "' , '" + Manager_Pwd + "' )";
					if (stat.executeUpdate(query) >= 1) {
						System.out.println("�����ȣ : " + Employee_IDX + " ������ ���� ���� ");
						return 1;
					} else
						return 0;
				}
			} else {// ���X
				System.out.println("����� �ƴմϴ�. ");
				return 2;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int employeeSignUp(int Employee_IDX, String Employee_NM, String Employee_DP, String Employee_CP) {
		// return 0 : �Է� ����
		// return 1 : �Է� ����
		// return 2 : �̹� ������� ��ϵ� -> �Է¾���

		try {
			ResultSet rs = stat.executeQuery(
					"select EXISTS (select * from EMPLOYEE where Employee_IDX = " + Employee_IDX + " ) as success");
			int flag = rs.getInt(1);
			if (flag == 0) { // ���X Ȯ��
				String query = "insert into EMPLOYEE ( Employee_IDX, Employee_NM, Employee_DP, Employee_CP ) "
						+ "values (" + Integer.toString(Employee_IDX) + ", '" + Employee_NM + "' , '" + Employee_DP
						+ "' , '" + Employee_CP + "' )";
				if (stat.executeUpdate(query) >= 1) {
					System.out.println("�����ȣ : " + Employee_IDX + " ���Լ��� ");
					return 1;
				} else
					return 0;

			} else {// ���O
				System.out.println("�̹� ������� ��ϵǾ� ����");
				return 2;
			}
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int LogIn(String Manager_ID, String Manager_Pwd) {
		// return 0 : �α��� ����
		// ������ �����ȣ ����
		ResultSet rs;
		int employee_IDX = 0;
		try {
			rs = stat.executeQuery("select * from MANAGER where Manager_ID = '" + Manager_ID + "' and Manager_Pwd = '"
					+ Manager_Pwd + "'");
			if (rs.next()) {
				employee_IDX = rs.getInt("Employee_IDX");
				System.out.println("�α��μ��� / �����ȣ : " + employee_IDX);
			} else {
				System.out.println("�α��� ���� ");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employee_IDX;
	}

	public int deleteEmployee(int Employee_IDX) {
		// return 0 : ���� ����
		// return 1 : ���� ����
		// return 2 : ��� X
		try {
			ResultSet rs = stat.executeQuery(
					"select EXISTS (select * from EMPLOYEE where Employee_IDX = " + Employee_IDX + " ) as success");
			int flag = rs.getInt(1);
			if (flag == 0) {// ���X
				System.out.println("����� �ƴ� ");
				return 2;
			} else { // ���O
				String query = "delete from employee where Employee_IDX = " + Employee_IDX;
				if (stat.executeUpdate(query) >= 1) {
					System.out.println("�����ȣ : " + Employee_IDX + " �������� ");
					return 1;
				} else
					return 0;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public ArrayList<String> getEmployeeData(String Employee_NM) {
		// return null : ����� �ƴ�
		// ArrayList[0] : Employee_IDX�� String���� ��ȯ�Ѱ�
		// ArrayList[1] : Employee_NM
		// ArrayList[2] : Employee_DP

		ArrayList returnArray = new ArrayList<String>();

		try {

			ResultSet rs = stat.executeQuery(
					"select EXISTS (select * from EMPLOYEE where Employee_NM = '" + Employee_NM + "' ) as success");
			int flag = rs.getInt(1);
			if (flag == 0) {// ���X
				System.out.println("����� �ƴ� ");
				return null;
			} else { // ���O
				rs = stat.executeQuery("select * from Employee where Employee_NM = '" + Employee_NM + "'");
				returnArray.add(Integer.toString(rs.getInt("Employee_IDX")));
				returnArray.add(rs.getString("Employee_NM"));
				returnArray.add(rs.getString("Employee_DP"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(returnArray.get(0) + " " + returnArray.get(1) + " " + returnArray.get(2));
		return returnArray;
	}

}