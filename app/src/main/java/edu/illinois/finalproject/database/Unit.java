package edu.illinois.finalproject.database;

/**
 * Created by gajan on 11/27/2017.
 */

public class Unit {
    private String name = "";
    private String keyToUnitOfProblems = "";

    public Unit() {
    }

    public Unit(String name, String keyToUnitOfProblems) {
        this.name = name;
        this.keyToUnitOfProblems = keyToUnitOfProblems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyToUnitOfProblems() {
        return keyToUnitOfProblems;
    }

    public void setKeyToUnitOfProblems(String keyToUnitOfProblems) {
        this.keyToUnitOfProblems = keyToUnitOfProblems;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                ", keyToUnitOfProblems='" + keyToUnitOfProblems + '\'' +
                '}';
    }
}
