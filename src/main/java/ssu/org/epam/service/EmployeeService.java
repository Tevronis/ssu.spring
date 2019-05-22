package ssu.org.epam.service;

import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;
import ssu.org.epam.model.Room;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getAllEmployeesNames();

    List<Employee> getEmployee(Long id, String name, String lastname);

    void addEmployee(Employee employee);

    void assignEmployee(Project project, Long userId);

    void unassignEmployee(Project project, Long userId);

    void upsalary(Long upsalary, Long userId);

    void transferEmployee(Long employeeId, Room to);

    void killEmployee(Long id);
}
