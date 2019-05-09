package ssu.org.epam.service;

import org.springframework.stereotype.Service;
import ssu.org.epam.model.DbHandler;
import ssu.org.epam.model.Employee;

import java.sql.SQLException;
import java.util.List;

@Service
public class OfficeManagementService {
    public Boolean addEmployee(Employee employee) {
        // verify
        // open db
        // set to db
        // close to db
        return true;
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
            System.out.println(name + " " + lastname);
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
        return "";
    }

    public String unassignEmployee(String project, Long userId) {
        return "";
    }

    public String upsalary(Long upsalary, Long userId) {
        return "";
    }

    public String transferEmployee(Long userId, Long from, Long to) {
        return "";
    }

    public String killEmployee(Long id) {
        return "";
    }
}
