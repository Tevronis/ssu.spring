package ssu.org.epam.service;

import org.springframework.stereotype.Service;
import ssu.org.epam.model.DbHandler;
import ssu.org.epam.model.Employee;
import ssu.org.epam.model.Project;

import java.sql.SQLException;
import java.util.List;

@Service
public class OfficeManagementService {

    public String addEmployee(Employee employee) {
        String validatin_response = employee.validation();
        if (!validatin_response.equals("ok")) {
            return validatin_response;
        }
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            dbHandler.addEmployee(employee);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String getAllEmployees(Boolean onlyfio) {
        StringBuilder result = new StringBuilder();
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            List<Employee> resp = dbHandler.getAllEmployees();
            for (Employee item: resp) {
                if (onlyfio)
                    result.append(item.getFIO()).append("\n");
                else
                    result.append(item.toString()).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

    public String getEmployee(Long id) {
        String errMsg = "Current employee have been not created.";
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            List<Employee> response = dbHandler.getEmployee(id);
            StringBuilder sb = new StringBuilder();
            if (response.isEmpty()) return errMsg;
            for (Employee item: response)
                sb.append(item.toString()).append("\n");
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return errMsg;
    }

    public String getEmployee(String name, String lastname) {
        String errMsg = "Current employee have been not created.";
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            List<Employee> response = dbHandler.getEmployee(name, lastname);
            StringBuilder sb = new StringBuilder();
            if (response.isEmpty()) return errMsg;
            for (Employee item: response)
                sb.append(item.toString()).append("\n");
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return errMsg;
    }

    public String assignEmployee(String project, Long userId) {
        String errMsg = "Current employee have been not created.";
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            dbHandler.assignEmployee(Project.valueOf(project), userId);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String unassignEmployee(String project, Long userId) {
        String errMsg = "Current employee have been not created.";
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            dbHandler.unassignEmployee(Project.valueOf(project), userId);
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
            DbHandler dbHandler = DbHandler.getInstance();
            dbHandler.upsalary(upsalary, userId);
            return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String transferEmployee(Long userId, Long to) {
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            if (dbHandler.transferEmployee(userId, to))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }

    public String killEmployee(Long id) {
        String errMsg = "Current employee have been not created.";
        try {
            DbHandler dbHandler = DbHandler.getInstance();
            if (dbHandler.deleteEmployee(id))
                return "ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "err";
    }
}
