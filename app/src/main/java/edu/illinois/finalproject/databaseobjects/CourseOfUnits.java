package edu.illinois.finalproject.databaseobjects;

import java.util.List;

/**
 * Created by gajan on 11/28/2017.
 */

public class CourseOfUnits {
    private List<Unit> unitsInCourse;

    public CourseOfUnits() {
    }

    public CourseOfUnits(List<Unit> unitsInCourse) {
        this.unitsInCourse = unitsInCourse;
    }

    public List<Unit> getUnitsInCourse() {
        return unitsInCourse;
    }

    public void setUnitsInCourse(List<Unit> unitsInCourse) {
        this.unitsInCourse = unitsInCourse;
    }

    @Override
    public String toString() {
        return "CourseOfUnits{" +
                "unitsInCourse=" + unitsInCourse +
                '}';
    }
}
