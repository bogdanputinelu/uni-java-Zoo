package DBServices;

import DBConnection.DBConnection;
import Employee.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeService {
    private static EmployeeService instance = null;
    private static Connection con = DBConnection.getDbConnection();
    private EmployeeService(){}

    public static EmployeeService getInstance()
    {
        if(instance == null)
        {
            instance = new EmployeeService();
        }
        return instance;
    }

    public void allEmployees(){
        try {
            PreparedStatement pr = con.prepareStatement("select * from employee");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + " " + rs.getString("firstName")
                        + " " + rs.getString("lastName") + " " + rs.getString("phoneNumber")
                        + " " + rs.getString("email"));
            }
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void addEmployee(Employee emp){
        try {
            PreparedStatement pr = con.prepareStatement("insert into employee(id, firstName, lastName, phoneNumber, email) values (?,?,?,?,?)");
            pr.setInt(1, emp.getEmployeeId());
            pr.setString(2, emp.getFirstName());
            pr.setString(3, emp.getLastName());
            pr.setString(4, emp.getPhoneNumber());
            pr.setString(5, emp.getEmail());
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteAllEmployees(){
        try{
            PreparedStatement pr = con.prepareStatement("delete from employee where id > 0");
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void deleteEmployee(int id){
        try{
            PreparedStatement pr = con.prepareStatement("delete from employee where id = ?");
            pr.setInt(1, id);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
    public void updatePhoneNumber(int id, String phoneNumber){
        try{
            PreparedStatement pr = con.prepareStatement("update employee set phoneNumber = ? where id = ?");
            pr.setInt(2, id);
            pr.setString(1, phoneNumber);
            pr.execute();
        }
        catch (SQLException e){
            System.out.println("sql error");
        }
    }
}
