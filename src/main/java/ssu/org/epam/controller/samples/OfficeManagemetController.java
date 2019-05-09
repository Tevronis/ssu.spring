package ssu.org.epam.controller.samples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssu.org.epam.model.DbHandler;
import ssu.org.epam.model.Employee;
import ssu.org.epam.service.OfficeManagementService;

import java.sql.SQLException;

@RestController
public class OfficeManagemetController {
    @Autowired
    private OfficeManagementService officeManagementService;

    @GetMapping(value = "/all_user_names")
    public String getAllUserNames(){
        return officeManagementService.getAllEmployees(true);
    }

    @GetMapping(value = "/all_users")
    public String getAllUsers(){
        return officeManagementService.getAllEmployees(false);
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public String getUser(@RequestParam(required = false) Long id,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String lastname){
        if (id != null) {
            return officeManagementService.getEmployee(id);
        }
        if (name != null && lastname != null) {
            return officeManagementService.getEmployee(name, lastname);
        }
        return "Bad request. See documentation!";
    }

    @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
    public String addUser(@RequestBody Employee employee){
        if (officeManagementService.addEmployee(employee))
            return "Сотрудник добавлен!";
        else
            return "Ошибка";
    }

    // by id
    @PostMapping(value = "/assignEmployee")
    public String assignEmployee(@RequestParam String project,
                                 @RequestParam(name = "id") Long userId){
        System.out.println(project + " " + userId);
        return project + " " + userId;
    }

    @PostMapping(value = "/unassignEmployee")
    public String unassignEmployee(@RequestParam String project,
                                   @RequestParam(name = "id") Long userId){
        System.out.println(project + " " + userId);
        return project + " " + userId;
    }

    @PostMapping(value = "/upsalary")
    public String setSalary(@RequestParam(name = "id") Long userId,
                            @RequestParam Long upsalary){
        return "'all users'";
    }

    @PostMapping(value = "/transferEmployee")
    public String transferEmployee(@RequestParam(name = "user") Long userId,
                                   @RequestParam(name = "from", required = false) Long fromRoom,
                                   @RequestParam(name = "to") Long toRoom){
        System.out.println("'" + userId + "' '" + fromRoom + "' '" + toRoom + "'");
        return "'" + userId + "' '" + fromRoom + "' '" + toRoom + "'";
    }

    @PostMapping(value = "/deleteEmployee")
    public String killEmployee(@RequestParam Long id){
        System.out.println(id);
        return "'all users'";
    }

    @PostMapping(value = "/admin/db/request")
    public String rawRequest(@RequestBody String request) throws SQLException {
        // TODO(menc): add authorization
        System.out.println(request);
        DbHandler dbHandler = DbHandler.getInstance();

        return dbHandler.request(request);
    }
}
