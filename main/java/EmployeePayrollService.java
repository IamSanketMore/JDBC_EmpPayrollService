import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}

    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;
    public EmployeePayrollService(){
        employeePayrollDBService=EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList=employeePayrollList;
    }

    public long readData(IOService ioService){
        if (ioService.equals(IOService.CONSOLE_IO)) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter Employee Name");
            String empName = scan.next();
            System.out.println("Enter Employee ID");
            int empID = scan.nextInt();
            System.out.println("Enter Employee Salary");
            int empSalary = scan.nextInt();

            EmployeePayrollData adder = new EmployeePayrollData( empID,empName, empSalary);
            employeePayrollList.add(adder);
            long result = employeePayrollList.size();
            return result;
        }else if(ioService.equals(IOService.FILE_IO)){
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
            return employeePayrollList.size();
        }else
            return 0;
    }

    public void empWriteData(IOService ioService){
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("OutPut\n"+employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    public void printData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().printData();
        else
            System.out.println("Chose File_IO");
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return 0;
    }

    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        employeePayrollService.empWriteData(IOService.FILE_IO);
        employeePayrollService.readData(IOService.FILE_IO);

    }

    public List<EmployeePayrollData> readEmpPayRollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name,salary);
        if (result == 0) return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayRollData(name);
        if (employeePayrollData != null) employeePayrollData.employeeSalary= (int) salary;
    }

    private EmployeePayrollData getEmployeePayRollData(String name) {
        for (EmployeePayrollData data : employeePayrollList) {
            if (data.employeeName.equals(name))
                return data;
        }
        return null;
    }

    public boolean checkEmployeePayRollSyncWithDB(String name) {
        List<EmployeePayrollData>employeePayrollDataList= employeePayrollDBService.getEmployeePayRollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayRollData(name));
    }

    public List<EmployeePayrollData> readFilteredEmpPayRollData(IOService ioService,String date,String date1) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readFilteredData(date,date1);
        return this.employeePayrollList;
    }
}