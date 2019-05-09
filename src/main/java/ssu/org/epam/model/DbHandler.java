package ssu.org.epam.model;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.*;

public class DbHandler {
    private static final String CON_STR = "jdbc:sqlite:C:/projects/restapi/src/main/resources/office.db";

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

    public String request(String q) throws SQLException {
//        ResultSet rs = this.connection.getMetaData().getTables(null, null, null, null);
//        while (rs.next()) {
//            System.out.println(rs.getString("TABLE_NAME"));
//        }
//        return "";
        // String sql = "SELECT * FROM EMPLOYEE";
        StringBuilder sb = new StringBuilder();
        try (Statement statement = this.connection.createStatement()) {
            // System.out.println("BEFORE");
            ResultSet resultSet = statement.executeQuery(q);
            // System.out.println("AFTER");
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

    public Project[] getProjectsById(int id) {
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
            // Если произошла ошибка - возвращаем пустую коллекцию
            return new Project[0];
        }
    }

    public String getRoomNameById(int id) {
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

    public String getPositionNameById(int id) {
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

    private List<Employee> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> result = new ArrayList<>();
        while (rs.next()) {
            result.add(new Employee(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("SURNAME"),
                            rs.getString("PATROYMIC"),
                            rs.getString("BIRTHDAY"),
                            rs.getString("EMAIL"),
                            rs.getDouble("SALARY"),
                            getProjectsById(rs.getInt("ID")),
                            Position.valueOf(getPositionNameById(rs.getInt("ID_POSITION"))),
                            Room.valueOf(getRoomNameById(rs.getInt("ID_ROOM")))
                    )
            );
        }
        return result;
    }

    public List<Employee> getAllEmployees() {
        try (Statement statement = this.connection.createStatement()) {
            List<Employee> employees = new ArrayList<Employee>();
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
        System.out.println(sql);
        try (Statement statement = this.connection.createStatement()) {
            System.out.println("here");
            ResultSet rs = statement.executeQuery(sql);
            return getEmployees(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
//
//    // Добавление продукта в БД
//    public void addProduct(Product product) {
//        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
//        try (PreparedStatement statement = this.connection.prepareStatement(
//                "INSERT INTO Products(`good`, `price`, `category_name`) " +
//                        "VALUES(?, ?, ?)")) {
//            statement.setObject(1, product.good);
//            statement.setObject(2, product.price);
//            statement.setObject(3, product.category_name);
//            // Выполняем запрос
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Удаление продукта по id
//    public void deleteProduct(int id) {
//        try (PreparedStatement statement = this.connection.prepareStatement(
//                "DELETE FROM Products WHERE id = ?")) {
//            statement.setObject(1, id);
//            // Выполняем запрос
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}