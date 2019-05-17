package ssu.org.epam.service;

import org.springframework.stereotype.Service;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class OfficeManagementService {

    public String addEmployee(Employee employee) {
        String validate_response = employee.validation();
        if (!validate_response.equals("ok")) {
            return validate_response;
        }
        try {
            DBService dbService = DBService.getInstance();
            dbService.addEmployee(employee);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public List<Employee> getAllEmployees() {
        try {
            DBService dbService = DBService.getInstance();
            return dbService.getAllEmployees();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Employee> getAllEmployeesNames() {
        try {
            DBService dbService = DBService.getInstance();
            return dbService.getAllEmployeesNames();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Employee> getEmployee(Long id) {
        try {
            DBService dbService = DBService.getInstance();
            return dbService.getEmployee(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Employee> getEmployee(String name, String lastname) {
        try {
            DBService dbService = DBService.getInstance();
            return dbService.getEmployee(name, lastname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public String assignEmployee(String project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.assignEmployee(Project.valueOf(project), userId);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String unassignEmployee(String project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.unassignEmployee(Project.valueOf(project), userId);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String upsalary(Long upsalary, Long userId) {
        if (upsalary < 0)
            return "decrease salary not valid case!";
        try {
            DBService dbService = DBService.getInstance();
            dbService.upsalary(upsalary, userId);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String transferEmployee(Long userId, Long to) {
        try {
            DBService dbService = DBService.getInstance();
            if (dbService.transferEmployee(userId, to))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String killEmployee(Long id) {
        try {
            DBService dbService = DBService.getInstance();
            if (dbService.deleteEmployee(id))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }
}
