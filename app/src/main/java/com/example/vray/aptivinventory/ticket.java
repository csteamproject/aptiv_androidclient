package com.example.vray.aptivinventory;

public class ticket {
    private int id;
    private String title;
    private String status;
    private int priority;
    private String description;



    public ticket(int id, String title, String status, int priority, String description) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.description = description;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
