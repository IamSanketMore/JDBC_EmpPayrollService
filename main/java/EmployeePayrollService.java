import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService
{
    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}

    public List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList)
    {
        this.employeePayrollList=employeePayrollList;
    }
    public EmployeePayrollService() {} //default Constructor

    public long readData(IOService ioService)
    {
        Scanner scanReader = new Scanner(System.in);
        if (ioService.equals(IOService.CONSOLE_IO))
        {
            System.out.println("Enter Employee ID");
            int empID = scanReader.nextInt();
            System.out.println("Enter Employee Name");
            String empName = scanReader.next();
            System.out.println("Enter Employee Salary");
            int empSalary = scanReader.nextInt();

            EmployeePayrollData adder = new EmployeePayrollData( empID,empName,empSalary);
            employeePayrollList.add(adder);
            long result = employeePayrollList.size();
            return result;
        }
        else if(ioService.equals(IOService.FILE_IO))
        {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
            return employeePayrollList.size();
        }
        else
            return 0;
    }

    public void empWriteData(IOService ioService)
    {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("OutPut\n"+employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    public void printData(IOService ioService)
    {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
        else
            System.out.println("Chose File_IO");
    }

    public long countEntries(IOService ioService)
    {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    //Read data from database
    public List<EmployeePayrollData> readEmployeePayRollData(IOService ioService)
    {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = new EmployeePayrollDBService().readData();
        return this.employeePayrollList;
    }
    public static void main(String[] args)
    {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.empWriteData(IOService.FILE_IO);
        employeePayrollService.readData(IOService.FILE_IO);

    }
}