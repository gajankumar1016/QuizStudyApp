package edu.illinois.finalproject.database;

/**
 * Created by gajan on 11/22/2017.
 */

public class Course {
    private String name = "";
    private String keyToCourseOfUnits = "";

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public Course(String name, String keyToCourseOfUnits) {
        this.name = name;
        this.keyToCourseOfUnits = keyToCourseOfUnits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyToCourseOfUnits() {
        return keyToCourseOfUnits;
    }

    public void setKeyToCourseOfUnits(String keyToCourseOfUnits) {
        this.keyToCourseOfUnits = keyToCourseOfUnits;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", keyToCourseOfUnits='" + keyToCourseOfUnits + '\'' +
                '}';
    }
}
