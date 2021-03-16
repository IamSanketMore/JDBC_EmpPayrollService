import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EmployeePayRollServiceTest
{

    @Test
    void given3EmpWhenWrittenToFilesShouldMatchEmpEntries()
    {
        EmployeePayrollData[] arrayOfEmp= {
                new EmployeePayrollData(1,"Sanket",1235),
                new EmployeePayrollData(2,"Bill",1235),
                new EmployeePayrollData(3,"Mark",1235),
        };
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmp));
        employeePayrollService.empWriteData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long result = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        System.out.println("Total Employee Entries :- "+result);
        Assertions.assertEquals(3,result);


    }
    //-----------------UC2--------------------------------------
    @Test
    public void givenEmployeePayrollDB_WhenRetrieved_ShouldMatchEmpCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        System.out.println(employeePayrollData);
        Assertions.assertEquals(6, employeePayrollData.size());
    }
    //-----------------UC3--------------------------------------
    @Test
    void givenNewSalaryForEmployee_whenUpdate_shouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 30000000.00);
        boolean result = employeePayrollService.checkEmployeePayRollSyncWithDB("Terisa");
        System.out.println(result);
        Assertions.assertEquals(true, result);
    }
}
