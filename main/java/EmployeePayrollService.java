import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {

    public Map<String,Double> readAveregeSalaryByGender(IOService ioService)
    {
        if(ioService.equals((IOService.DB_IO)))
            return employeePayrollDBService.getAverageSalaryByGender();
        return null;
    }


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
            //System.out.println("Chose File_IO");
        System.out.println(employeePayrollList);

    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            return new EmployeePayrollFileIOService().countEntries();
        return employeePayrollList.size();
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

  //  public List<EmployeePayrollData> readFilteredEmpPayRollData(IOService ioService,String startDate,String endDate) {
        public List<EmployeePayrollData> readFilteredEmpPayRollData(IOService ioService) {
            if (ioService.equals(IOService.DB_IO))
            //this.employeePayrollList = employeePayrollDBService.readFilteredData(startDate,endDate);
            this.employeePayrollList = employeePayrollDBService.readFilteredData();
        return this.employeePayrollList;
    }

    public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate)
    {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate,endDate);
        return null;
    }

    public void addEmployee(String name, String gender, double salary, LocalDate date) {
        employeePayrollList.add(employeePayrollDBService.addEmployeeData(name,gender,salary,date));
    }
    public void addEmployee(List<EmployeePayrollData> employeePayrollDataList) {
        employeePayrollDataList.forEach(employeePayrollData ->{
            System.out.println("Emp Being Added: "+employeePayrollData.employeeName);
            this.addEmployee(employeePayrollData.employeeName,employeePayrollData.gender,employeePayrollData.employeeSalary,employeePayrollData.start);
            System.out.println("Emp Added: "+employeePayrollData.employeeName);
        });
        System.out.println(employeePayrollList);
    }
    public void addEmployeeToDBWithThreads(List<EmployeePayrollData> employeePayrollDataList) {
        Map<Integer,Boolean> empAdditionStatus = new HashMap<Integer,Boolean>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () ->{
                empAdditionStatus.put(employeePayrollData.hashCode(),false);
                System.out.println("Employee Being Added : " + Thread.currentThread().getName());
                this.addEmployee(employeePayrollData.employeeName,employeePayrollData.gender,employeePayrollData.employeeSalary,employeePayrollData.start);
                empAdditionStatus.put(employeePayrollData.hashCode(),true);
                System.out.println("Employee Being Added : " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData.employeeName);
            thread.start();
        });
        while (empAdditionStatus.containsValue(false)){
            try{Thread.sleep(10);
            }catch  (InterruptedException e){}
        }
        System.out.println(employeePayrollDataList);
    }
}