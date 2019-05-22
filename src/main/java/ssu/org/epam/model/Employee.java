package ssu.org.epam.model;

import ssu.org.epam.exception.OfficeException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Employee {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    private String dateOfBirth;
    private String email;
    private Integer salary;
    private Project[] projects;
    private Position position;
    private Room room;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDateOfBirth() {
        return calculateAge(dateOfBirth);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {

        this.projects = projects;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void validation() {
        Pattern pat = Pattern.compile("^[0-9]{1,2}.[0-9]{1,2}.[0-9]{4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pat.matcher(this.dateOfBirth);
        if (!matcher.find())
            throw new OfficeException("Incorrect date. format: [dd.MM.yyyy]");

        matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(this.email);
        if (!matcher.find())
            throw new OfficeException("Incorrect email. [A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}");
    }

    private static String calculateAge(final String birthday) {
        try {
            Date date1 = new SimpleDateFormat("dd.MM.yyyy").parse(birthday);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.setTime(date1);
            dob.add(Calendar.DAY_OF_MONTH, -1);

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) <= dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return String.valueOf(age);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfBirth='" + calculateAge(dateOfBirth) + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", projects=" + Arrays.toString(projects) +
                ", position=" + position +
                ", room=" + room +
                '}';
    }
}
