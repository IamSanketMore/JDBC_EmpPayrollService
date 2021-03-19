import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.catalog.Catalog;

public class EmployeePayrollDBService {
    private int connectionCounter=0;
    private PreparedStatement employeePayRollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    private EmployeePayrollDBService(){}

    public static EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService==null)
            employeePayrollDBService=new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        connectionCounter++;
        String jdbcULR = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Sanket@123";
        Connection connection;
        System.out.println("Processing Thread: " + Thread.currentThread().getName()+
                "Connection To DB With ID: " + connectionCounter);
        connection = DriverManager.getConnection(jdbcULR,userName,password);
//add Console logs
        System.out.println("Processing Thread: " + Thread.currentThread().getName()+
                "ID: " + connectionCounter+ " Connection SuccessFul..!!"+connection);
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
    public EmployeePayrollData addEmployeeDataUC7(String name, String gender, double salary, LocalDate date) {
        int employeeID = -1;
        String sql = String.format("insert into employee_payroll (name,gender,salary,start) values ('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(date));
        EmployeePayrollData employeePayrollData = null;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeID = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData( employeeID,name, salary, gender, date);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
    }

    public EmployeePayrollData addEmployeeData(String name, String gender, double salary, LocalDate date) {
        int employeeID = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into employee_payroll (name,gender,salary,start) values ('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(date));
            int rowAffected = statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                return employeePayrollData;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary-deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary-tax;
            String sql = String.format("insert into payroll_details (employee_id, basic_pay, deductions, taxable_pay, tax, net_pay ) values " +
                    "( %s,%s,%s,%s,%s,%s)", employeeID, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected==1){
                employeePayrollData = new EmployeePayrollData( employeeID,name, salary, gender, date);
            }
        }catch (SQLException e){
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        try {
            connection.commit();
        }catch (SQLException exception) {
            exception.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException r){
                    r.printStackTrace();
                }
            }
        }
        return employeePayrollData;
    }



}