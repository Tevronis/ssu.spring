package ssu.org.epam.model;

import java.util.Arrays;

public class Test {
    private String f;
    private Long cnt;
    private Project[] projects;

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "Test{" +
                "f='" + f + '\'' +
                ", cnt=" + cnt +
                ", projects=" + Arrays.toString(projects) +
                '}';
    }
}
