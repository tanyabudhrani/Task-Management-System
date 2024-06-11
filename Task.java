package hk.edu.polyu.comp.comp2021.tms.model;
import java.io.*;
import java.util.*;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private double duration;
    private List<Task> prerequisites;

    public Task(String name, String description, double duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.prerequisites = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getDuration() {
        return duration;
    }

    public List<Task> getPrerequisites() {
        return prerequisites;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setDuration(double newDuration) {
        this.duration = newDuration;
    }

    public void setPrerequisites(List<Task> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void addPrerequisite(Task prerequisite) {
        prerequisites.add(prerequisite);
    }

    public void removePrerequisite(Task prerequisite) {
        prerequisites.remove(prerequisite);
    }
}

