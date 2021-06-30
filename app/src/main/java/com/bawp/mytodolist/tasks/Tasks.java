package com.bawp.mytodolist.tasks;

public class Tasks {
    private long id;
    private String taskTitle;
    private String taskDetails;

    public Tasks(String taskTitle, String taskDetails) {
        this.taskTitle = taskTitle;
        this.taskDetails = taskDetails;
    }

    public Tasks() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }
}
