package ssu.org.epam.controller.samples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ssu.org.epam.service.DBService;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Test;
import ssu.org.epam.service.OfficeManagementService;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RestController
public class OfficeManagemetController {
    @Autowired
    private OfficeManagementService officeManagementService;

    @GetMapping(value = "/all_user_names")
    public List<Employee> getAllUserNames(){
        return officeManagementService.getAllEmployeesNames();
    }

    @GetMapping(value = "/all_users")
    public List<Employee> getAllUsers(){
        return officeManagementService.getAllEmployees();
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public List<Employee> getUser(@RequestParam(required = false) Long id,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String lastname){
        if (id != null) {
            return officeManagementService.getEmployee(id);
        }
        if (name != null && lastname != null) {
            return officeManagementService.getEmployee(name, lastname);
        }
        return Collections.emptyList();
    }

    @PostMapping(value = "/user")
    public String addUser(@RequestBody Employee employee){
        return officeManagementService.addEmployee(employee);
    }

//    @PostMapping(value = "/user")
//    public String addUser(@RequestBody HttpServletResponse response){
//        try {
//            Employee employee = new Employee();
//            String json = new ObjectMapper().writeValueAsString(employee);
//            response.getWriter().write(json);
//            response.flushBuffer();
//            return officeManagementService.addEmployee(employee);
//        }
//        catch (Exception exc) {
//            return "error!";
//        }
//    }

    @PostMapping(value = "/assignEmployee")
    public String assignEmployee(@RequestParam String project,
                                 @RequestParam(name = "id") Long userId){
        return officeManagementService.assignEmployee(project, userId);
    }

    @PostMapping(value = "/unassignEmployee")
    public String unassignEmployee(@RequestParam String project,
                                   @RequestParam(name = "id") Long userId){
        return officeManagementService.unassignEmployee(project, userId);
    }

    @PostMapping(value = "/upsalary")
    public String setSalary(@RequestParam(name = "id") Long userId,
                            @RequestParam Long upsalary){
        return officeManagementService.upsalary(upsalary, userId);
    }

    @PostMapping(value = "/transferEmployee")
    public String transferEmployee(@RequestParam(name = "user") Long userId,
                                   @RequestParam(name = "to") Long toRoom){
        return officeManagementService.transferEmployee(userId, toRoom);
    }

    @PostMapping(value = "/deleteEmployee")
    public String killEmployee(@RequestParam Long id){
        return officeManagementService.killEmployee(id);
    }

    @PostMapping(value = "/admin/db/request")
    public String rawRequest(@RequestBody String request) throws SQLException {
        // TODO(menc): add authorization
        System.out.println(request);
        DBService dbService = DBService.getInstance();

        return dbService.request(request);
    }

    @PostMapping(value = "/test")
    public String test(@RequestBody Test t) {
        System.out.println(t.toString());
        return t.toString();
    }
}
