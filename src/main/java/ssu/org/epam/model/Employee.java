package ssu.org.epam.model;

import java.util.Arrays;

public class Employee {
    private Integer id;
    private String name;
    private String surname;
    private String patronymic;
    private String dateOfBirth;
    private String email;
    private Double salary;
    private Project[] projects;
    private Position position;
    private Room room;

    public Employee(Integer id, String name, String surname, String patronymic, String dateOfBirth, String email, Double salary, Project[] projects, Position position, Room room) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.salary = salary;
        this.projects = projects;
        this.position = position;
        this.room = room;
    }

    public String getFIO() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", projects=" + Arrays.toString(projects) +
                ", position=" + position +
                ", room=" + room +
                '}';
    }
}
