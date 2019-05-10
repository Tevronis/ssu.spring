package ssu.org.epam.model;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.*;

public class DbHandler {
    private static final String CON_STR = "jdbc:sqlite:src/main/resources/office.db";

    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private Connection connection;

    private DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    private List<Employee> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> result = new ArrayList<>();
        while (rs.next()) {
            result.add(Employee.createEmployee(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("SURNAME"),
                            rs.getString("PATROYMIC"),
                            rs.getString("BIRTHDAY"),
                            rs.getString("EMAIL"),
                            rs.getInt("SALARY"),
                            getProjectsById(rs.getInt("ID")),
                            Position.valueOf(getPositionNameById(rs.getInt("ID_POSITION"))),
                            Room.valueOf(getRoomNameById(rs.getInt("ID_ROOM")))
                    )
            );
        }
        return result;
    }

    private Project[] getProjectsById(int id) {
        String sql = String.format(
                "SELECT name FROM Projects WHERE id IN (SELECT id_project FROM EmployeeProject WHERE id_employee = %d)",
                id
        );
        try (Statement statement = this.connection.createStatement()) {
            List<Project> projects = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                projects.add(Project.valueOf(resultSet.getString("name")));
            }
            Project[] res = new Project[projects.size()];
            return projects.toArray(res);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Project[0];
        }
    }

    private String getRoomNameById(int id) {
        String sql = String.format("SELECT name FROM Rooms WHERE id = %d", id);
        try (Statement statement = this.connection.createStatement()) {
            String result = "";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result = resultSet.getString("name");
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getPositionNameById(int id) {
        String sql = String.format("SELECT name FROM Position WHERE id = %d", id);
        try (Statement statement = this.connection.createStatement()) {
            String result = "";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result = resultSet.getString("name");
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
// ################ PUBLIC GETS ###################
    public String request(String q) throws SQLException {
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

    public List<Employee> getAllEmployees() {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM Employee");
            return getEmployees(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Employee> getEmployee(Long id) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM Employee WHERE id = %d", id));
            return getEmployees(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Employee> getEmployee(String name, String lastname) {
        String sql = String.format("SELECT * FROM Employee WHERE NAME = '%s' and surname = '%s'", name, lastname);
        try (Statement statement = this.connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return getEmployees(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
// ################ PUBLIC POSTS ##################

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO Employee" +
                "(`NAME`, `SURNAME`, `PATROYMIC`, `BIRTHDAY`, `EMAIL`, `SALARY`, `ID_ROOM`, `ID_POSITION`) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println(sql);
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, employee.getName());
            statement.setObject(2, employee.getSurname());
            statement.setObject(3, employee.getPatronymic());
            statement.setObject(4, employee.getDateOfBirth());
            statement.setObject(5, employee.getEmail());
            statement.setObject(6, employee.getSalary());
            statement.setObject(7, employee.getRoom().ordinal() + 1);
            statement.setObject(8, employee.getPosition().ordinal() + 1);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignEmployee(Project project, Long userId) {
        String sql = "INSERT INTO EmployeeProject(`ID_EMPLOYEE`, `ID_PROJECT`) VALUES(?, ?)";
        System.out.println(sql);
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
        String sql = "UPDATE employee SET salary = salary + ? WHERE id = ?";
        System.out.println(sql);
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setObject(2, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean transferEmployee(Long userId, Long to) {
        String sql = "UPDATE employee SET ID_ROOM = ? WHERE id = ?";
        System.out.println(sql);
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, to);
            statement.setObject(2, userId);
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteEmployee(Long id) {
        String sql = "DELETE FROM Employee WHERE ID = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}