import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    //------------------- UC4 ------------------------------------------
    @Test
    void givenDateRangeToEmployeePayRollInDB_WhenRetrieved_ShouldMatchFilteredEmployeeCount()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        //  String startDate = "2018-01-01";
        //   String endDate= "2020-12-31";
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readFilteredEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertEquals(5,employeePayrollData.size());
    }

    //-------------------UC5 ------------------------------------------
    @Test
    void  givenDateRangeToEmployeePayRollInDB_WhenRetrieved_ShouldMatchEmployeeCount()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
      //  List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData =
                employeePayrollService.readEmployeePayrollForDateRange(EmployeePayrollService.IOService.DB_IO,startDate,endDate);
        Assertions.assertEquals(4,employeePayrollData.size());
    }

    //------------------------- UC-6 ------------------------------------------
    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProper()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        Map<String,Double> averageSalaryByGender = employeePayrollService.readAveregeSalaryByGender(EmployeePayrollService.IOService.DB_IO);

        Assertions.assertTrue(averageSalaryByGender.get("M").equals(262000.00) &&
        averageSalaryByGender.get("F").equals(30000000.00));
    }


    @Test
    void givenNewEmployeeToEmployeeRollDB_whenAdded_shouldSyncWithDB ()
    {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmpPayRollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployee("MARK","M",50000000.00,LocalDate.now());
        boolean result = employeePayrollService.checkEmployeePayRollSyncWithDB("MARK");
        Assertions.assertTrue(result);
    }
}
