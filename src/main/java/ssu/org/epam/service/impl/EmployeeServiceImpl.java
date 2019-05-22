package ssu.org.epam.service.impl;

import org.springframework.stereotype.Service;
import ssu.org.epam.exception.OfficeException;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;
import ssu.org.epam.model.Room;
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
                List<Employee> result = dbService.getEmployee(id);
                if (result.isEmpty())
                    throw new OfficeException(String.format("Employee with id = %d not existing", id));
                return result;
            }
            if (name != null && lastname != null) {
                List<Employee> result = dbService.getEmployee(name, lastname);
                if (result.isEmpty())
                    throw new OfficeException(String.format("Employee with name, lastname {%s, %s} not existing", name, lastname));
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new OfficeException(String.format("Incorrect arguments: id, name, lastname {%d, %s, %s}", id, name, lastname));
    }

    public void assignEmployee(Project project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.assignEmployee(project, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unassignEmployee(Project project, Long userId) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.unassignEmployee(project, userId);
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

    public void transferEmployee(Long employeeId, Room to) {
        try {
            DBService dbService = DBService.getInstance();
            dbService.transferEmployee(employeeId, to);
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
