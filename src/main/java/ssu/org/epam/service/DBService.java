package ssu.org.epam.service;

import org.sqlite.JDBC;
import ssu.org.epam.exception.OfficeException;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;
import ssu.org.epam.model.Room;

import java.sql.*;
import java.util.*;

public class DBService {
    private static final String CON_STR = "jdbc:sqlite:src/main/resources/office.db";

    private static DBService instance = null;

    public static synchronized DBService getInstance() throws SQLException {
        if (instance == null)
            instance = new DBService();
        return instance;
    }

    private Connection connection;

    private DBService() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public Integer getInt(ResultSet rs, String label) {
        try {
            return rs.getInt(label);
        } catch (SQLException e) {
            // e.printStackTrace();
            return null;
        }
    }

    public String getString(ResultSet rs, String label) {
        try {
            return rs.getString(label);
        } catch (SQLException e) {
            // e.printStackTrace();
            return null;
        }
    }

    // ################ PUBLIC GETS ###################
    public String request(String q) {
        StringBuilder sb = new StringBuilder();
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(q);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1)
                        sb.append(",  ");
                    String columnValue = resultSet.getString(i);
                    sb.append(columnValue).append(" ").append(rsmd.getColumnName(i));
                }
                sb.append("\n");
            }
            return sb.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Wtf is query?!";
        }
    }

    public ResultSet getResultSet(String query) throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery(query);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> result = new ArrayList<>();
        MapService mapService = new MapService();
        ResultSet rs = getResultSet("SELECT * FROM Employee");
        while (rs.next()) {
            Employee employee = new Employee();
            mapService.mapEmployee(rs, employee);
            mapService.mapProjects(getResultSet(String.format(
                    "SELECT name FROM Projects WHERE id IN (SELECT id_project FROM EmployeeProject WHERE id_employee = %d)",
                    employee.getRoom().ordinal()+1)), employee);
            result.add(employee);
        }
        return result;
    }

    public List<Employee> getAllEmployeesNames() throws SQLException {
        List<Employee> result = new ArrayList<>();
        MapService mapService = new MapService();
        ResultSet rs = getResultSet("SELECT name, surname, patroymic FROM Employee");
        while (rs.next()) {
            Employee employee = new Employee();
            mapService.mapEmployee(rs, employee);
            result.add(employee);
        }
        return result;
    }

    public List<Employee> getEmployee(Long id) throws SQLException {
        List<Employee> result = new ArrayList<>();
        MapService mapService = new MapService();
        ResultSet rs = getResultSet(String.format("SELECT * FROM Employee WHERE id = %d", id));
        while (rs.next()) {
            Employee employee = new Employee();
            mapService.mapEmployee(rs, employee);
            mapService.mapProjects(getResultSet(String.format(
                    "SELECT name FROM Projects WHERE id IN (SELECT id_project FROM EmployeeProject WHERE id_employee = %d)",
                    employee.getRoom().ordinal()+1)), employee);
            result.add(employee);
        }

        return result;
    }

    public List<Employee> getEmployee(String name, String lastname) throws SQLException {
        List<Employee> result = new ArrayList<>();
        MapService mapService = new MapService();
        ResultSet rs = getResultSet(String.format("SELECT * FROM Employee WHERE NAME = '%s' and surname = '%s'", name, lastname));
        while (rs.next()) {
            Employee employee = new Employee();
            mapService.mapEmployee(rs, employee);
            mapService.mapProjects(getResultSet(String.format(
                    "SELECT name FROM Projects WHERE id IN (SELECT id_project FROM EmployeeProject WHERE id_employee = %d)",
                    employee.getRoom().ordinal()+1)), employee);
            result.add(employee);
        }

        return result;
    }
// ################ PUBLIC POSTS ##################

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO Employee" +
                "(`NAME`, `SURNAME`, `PATROYMIC`, `BIRTHDAY`, `EMAIL`, `SALARY`, `ID_ROOM`, `ID_POSITION`) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        MapService mapService = new MapService();
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            mapService.mapStatementFromEmployee(employee, statement);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignEmployee(Project project, Long userId) {
        String sql = "INSERT INTO EmployeeProject(`ID_EMPLOYEE`, `ID_PROJECT`) VALUES(?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, project.ordinal() + 1);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unassignEmployee(Project project, Long userId) {
        String sql = "DELETE FROM EmployeeProject WHERE ID_EMPLOYEE = ? and ID_PROJECT = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, userId);
            statement.setObject(2, project.ordinal() + 1);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upsalary(Long value, Long userId) {
        String sql = "UPDATE employee SET salary = ? WHERE id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setObject(2, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferEmployee(Long userId, Room to) {
        String sql = "UPDATE employee SET ID_ROOM = ? WHERE id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, to.ordinal()+1);
            statement.setObject(2, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Long id) {
        String sql = "DELETE FROM Employee WHERE ID = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}