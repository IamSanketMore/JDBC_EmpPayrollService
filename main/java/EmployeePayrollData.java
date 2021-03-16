import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData
{
    public String employeeName;
    public int employeeID;
    public int employeeSalary;
    public  double employeeSalary1;
    public String gender;
    public LocalDate start;

    public EmployeePayrollData(String employeeName, int employeeID, double employeeSalary, String gender, LocalDate start)
    {
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.employeeSalary1 = employeeSalary;
        this.gender = gender;
        this.start = start;
    }

    public EmployeePayrollData( int employeeID, String employeeName,int employeeSalary) {

        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    @Override
    public String toString()
    {
        return "EmployeePayrollData{" +
                "employeeName='" + employeeName + '\'' +
                ", employeeID=" + employeeID +
                ", employeeSalary=" + employeeSalary +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return employeeID == that.employeeID
                && employeeSalary == that.employeeSalary
                && Objects.equals(employeeName, that.employeeName)
                && Objects.equals(gender, that.gender)
                && Objects.equals(start, that.start);
    }
}