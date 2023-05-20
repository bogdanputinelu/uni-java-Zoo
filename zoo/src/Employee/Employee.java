package Employee;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private static int id = 0;
    private int employeeId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<Integer> enclosureIds;

    public Employee(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.enclosureIds = new ArrayList<>();
        id++;
        this.employeeId = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void addResponsibility(Integer enclosureId){
        enclosureIds.add(enclosureId);
    }
    public void removeResponsibility(Integer enclosureId){
        enclosureIds.remove(enclosureId);
    }

    public List<Integer> getEnclosureIds() {
        return enclosureIds;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " with id: " + this.employeeId;
    }
}
