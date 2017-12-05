package edu.illinois.finalproject.DatabaseObjects;

/**
 * Created by gajan on 11/22/2017.
 */

public class Problem {
    private String problem = "";
    private String answer = "";
    private String solution = "";
    private String username = "";

    public Problem() {
    }

    public Problem(String problem, String solution) {
        this.problem = problem;
        this.solution = solution;
    }

    public Problem(String problem, String answer, String solution) {
        this.problem = problem;
        this.answer = answer;
        this.solution = solution;
    }

    public Problem(String problem, String answer, String solution, String username) {
        this.problem = problem;
        this.answer = answer;
        this.solution = solution;
        this.username = username;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "problem='" + problem + '\'' +
                ", answer='" + answer + '\'' +
                ", solution='" + solution + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Problem problem1 = (Problem) o;

        if (!problem.equals(problem1.problem)) return false;
        if (!answer.equals(problem1.answer)) return false;
        if (!solution.equals(problem1.solution)) return false;
        return username.equals(problem1.username);
    }

    @Override
    public int hashCode() {
        int result = problem.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + solution.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
