import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayRollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    private EmployeePayrollDBService(){}

    public static EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService==null)
            employeePayrollDBService=new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcULR = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Sanket@123";
        Connection connection;
        System.out.println("Connecting To DB: " + jdbcULR);
        connection = DriverManager.getConnection(jdbcULR,userName,password);
        System.out.println("Connection is successful..! " + connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "select * from employee_payroll;";
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    public List<EmployeePayrollData> getEmployeePayRollData(String name) {
        List<EmployeePayrollData>employeePayrollDataList = null;
        if (this.employeePayRollDataStatement==null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayRollDataStatement.setString(1,name);
            ResultSet resultSet = employeePayRollDataStatement.executeQuery();
            employeePayrollDataList = this.getEmployeePayRollData(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> getEmployeePayRollData(ResultSet resultSet) {
        List<EmployeePayrollData>employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("salary");
                LocalDate date = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id,name,salary,gender,date));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    //Prepared Statement-------------------------
    private void preparedStatementForEmployeeData()
    {
        try
        {
            Connection connection;
            connection = this.getConnection();
            String sql = "select * from employee_payroll where name= ?;";
            employeePayRollDataStatement = connection.prepareStatement(sql);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    //public List<EmployeePayrollData> readFilteredData(String startDate,String endDate) {
        public List<EmployeePayrollData> readFilteredData() {
       // String sql = String.format("select * from employee_payroll where start between '%s' and '%s';",startDate,endDate);
        String sql = String.format("select * from employee_payroll where start between cast('2017-01-01' as date) and date(now());");
            return this.getEmployeePayrollDataUsingDB(sql);
    }

    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {

      String sql = String.format("select * from employee_payroll where start between '%s' and '%s';"
              ,Date.valueOf(startDate),Date.valueOf(endDate));
      return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollDataList = this.getEmployeePayRollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;

    }

    public Map<String, Double> getAverageSalaryByGender() {
    String sql = "SELECT gender,AVG(salary) as avg_salary FROM employee_payroll GROUP  BY gender;";
    Map<String,Double> genderToAverageSalaryMap = new HashMap<>();

        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String gender = resultSet.getString("gender");
                double salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender,salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }


}