package ssu.org.epam.service.impl;

import org.springframework.stereotype.Service;
import ssu.org.epam.exception.OfficeException;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;
import ssu.org.epam.service.DBService;
import ssu.org.epam.service.EmployeeService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    public void addEmployee(Employee employee) {

        employee.validation();

        try {
            DBService dbService = DBService.getInstance();
            dbService.addEmployee(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public List<Employee> getEmployee(Long id, String name, String lastname) {
        try {
            DBService dbService = DBService.getInstance();
            if (id != null) {
                return dbService.getEmployee(id);
            }
            if (name != null && lastname != null) {
                return dbService.getEmployee(name, lastname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public void assignEmployee(String project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.assignEmployee(Project.valueOf(project), userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unassignEmployee(String project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.unassignEmployee(Project.valueOf(project), userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upsalary(Long upsalary, Long userId) {
        if (upsalary < 0)
            throw new OfficeException("Decrease salary not valid case!");
        try {
            DBService dbService = DBService.getInstance();
            dbService.upsalary(upsalary, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferEmployee(Long userId, Long to) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.transferEmployee(userId, to);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void killEmployee(Long id) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.deleteEmployee(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
