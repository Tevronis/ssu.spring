package ssu.org.epam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssu.org.epam.service.DBService;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Test;
import ssu.org.epam.service.EmployeeService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RestController
public class OfficeManagemetController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/all_user_names")
    public List<Employee> getAllUserNames(){
        return employeeService.getAllEmployeesNames();
    }

    @GetMapping(value = "/all_users")
    public List<Employee> getAllUsers(){
        return employeeService.getAllEmployees();
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public List<Employee> getUser(@RequestParam(required = false) Long id,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String lastname){
        return employeeService.getEmployee(id, name, lastname);
    }

    @PostMapping(value = "/user")
    public void addUser(@RequestBody Employee employee){
        employeeService.addEmployee(employee);
    }

//    @PostMapping(value = "/user")
//    public String addUser(@RequestBody HttpServletResponse response){
//        try {
//            Employee employee = new Employee();
//            String json = new ObjectMapper().writeValueAsString(employee);
//            response.getWriter().write(json);
//            response.flushBuffer();
//            return employeeService.addEmployee(employee);
//        }
//        catch (Exception exc) {
//            return "error!";
//        }
//    }

    @PostMapping(value = "/assignEmployee")
    public void assignEmployee(@RequestParam String project,
                                 @RequestParam(name = "id") Long userId){
        employeeService.assignEmployee(project, userId);
    }

    @PostMapping(value = "/unassignEmployee")
    public void unassignEmployee(@RequestParam String project,
                                   @RequestParam(name = "id") Long userId){
        employeeService.unassignEmployee(project, userId);
    }

    @PostMapping(value = "/upsalary")
    public void setSalary(@RequestParam(name = "id") Long userId,
                            @RequestParam Long upsalary){
        employeeService.upsalary(upsalary, userId);
    }

    @PostMapping(value = "/transferEmployee")
    public void transferEmployee(@RequestParam(name = "user") Long userId,
                                   @RequestParam(name = "to") Long toRoom){
        employeeService.transferEmployee(userId, toRoom);
    }

    @PostMapping(value = "/deleteEmployee")
    public void killEmployee(@RequestParam Long id){
        employeeService.killEmployee(id);
    }

    @PostMapping(value = "/admin/db/request")
    public String rawRequest(@RequestBody String request) throws SQLException {
        // TODO(menc): add authorization
        System.out.println(request);
        DBService dbService = DBService.getInstance();

        return dbService.request(request);
    }
}
