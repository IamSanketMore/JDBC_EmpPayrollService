import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {
    public int employeeID;
    public String employeeName;
    public double employeeSalary;
    public String gender;
    public LocalDate start;

    public EmployeePayrollData( int employeeID,String employeeName, double employeeSalary, String gender, LocalDate start)
    {
        this(employeeID,employeeName,employeeSalary);
        this.gender = gender;
        this.start = start;
    }

    public EmployeePayrollData(int employeeID,String employeeName,  double employeeSalary)
    {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    @Override
    public String toString()
    {
        return "EmployeePayrollData{" +
                "employeeID=" + employeeID +
                ", employeeName='" + employeeName + '\'' +
                ", employeeSalary=" + employeeSalary +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(employeeName, employeeID, employeeSalary, gender, start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return employeeID == that.employeeID && Double.compare(that.employeeSalary, employeeSalary) == 0 && Objects.equals(employeeName, that.employeeName) && Objects.equals(gender, that.gender) && Objects.equals(start, that.start);
    }
}