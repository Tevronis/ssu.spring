package ssu.org.epam.service;

import ssu.org.epam.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getAllEmployeesNames();

    List<Employee> getEmployee(Long id, String name, String lastname);

    void addEmployee(Employee employee);

    void assignEmployee(String project, Long userId);

    void unassignEmployee(String project, Long userId);

    void upsalary(Long upsalary, Long userId);

    void transferEmployee(Long userId, Long to);

    void killEmployee(Long id);
}
