package ssu.org.epam.service;

import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Position;
import ssu.org.epam.model.Project;
import ssu.org.epam.model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapService {

    public void mapEmployee(ResultSet rs, Employee employee) throws SQLException {
        DBService dbService = DBService.getInstance();
        employee.setId(dbService.getInt(rs, "ID"));
        employee.setName(dbService.getString(rs, "NAME"));
        employee.setSurname(dbService.getString(rs, "SURNAME"));
        employee.setPatronymic(dbService.getString(rs, "PATROYMIC"));
        employee.setDateOfBirth(dbService.getString(rs, "BIRTHDAY"));
        employee.setEmail(dbService.getString(rs, "EMAIL"));
        employee.setSalary(dbService.getInt(rs, "SALARY"));

        Integer id_room =  dbService.getInt(rs, "ID_ROOM");
        Integer id_position =  dbService.getInt(rs, "ID_POSITION");
        if (id_room != null) {
            mapPosition(dbService.getResultSet(String.format(
                    "SELECT name FROM Position WHERE id = %d", id_room)), employee);
        }
        if (id_position != null) {
            mapRoom(dbService.getResultSet(String.format(
                    "SELECT name FROM Rooms WHERE id = %d", id_position)), employee);
        }
    }

    public void mapProjects(ResultSet rs, Employee employee) throws SQLException {
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            projects.add(Project.valueOf(rs.getString("name")));
        }
        Project[] res = new Project[projects.size()];
        employee.setProjects(projects.toArray(res));
    }

    public void mapRoom(ResultSet rs, Employee employee) throws SQLException {
        rs.next();  // rs может быть пустым (не нашел ни одного соответствия в Room)
        employee.setRoom(Room.valueOf(rs.getString("name")));
    }

    public void mapPosition(ResultSet rs, Employee employee) throws SQLException {
        rs.next();
        employee.setPosition(Position.valueOf(rs.getString("name")));
    }

    public void mapStatementFromEmployee(Employee employee, PreparedStatement statement) throws SQLException {
        statement.setObject(1, employee.getName());
        statement.setObject(2, employee.getSurname());
        statement.setObject(3, employee.getPatronymic());
        statement.setObject(4, employee.getDateOfBirth());
        statement.setObject(5, employee.getEmail());
        statement.setObject(6, employee.getSalary());
        statement.setObject(7, employee.getRoom().ordinal() + 1);
        statement.setObject(8, employee.getPosition().ordinal() + 1);
    }

}
