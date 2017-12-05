package edu.illinois.finalproject.databaseobjects;

import java.util.List;

/**
 * Created by gajan on 11/28/2017.
 */

public class UnitOfProblems {
    private List<Problem> problems;

    public UnitOfProblems() {
    }

    public UnitOfProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    @Override
    public String toString() {
        return "UnitOfProblems{" +
                "problems=" + problems +
                '}';
    }
}
