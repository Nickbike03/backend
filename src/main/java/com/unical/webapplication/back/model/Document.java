package com.unical.webapplication.back.model;

public class Document {
    private int id;
    private int user_id;
    private int validated_admin;
    private String name;
    private String description;
    private byte[] data; 
    private String course;
    private int size;
    private boolean validated;

    public Document(){}

    public Document(int user_id, String name, String description, byte[] data, String course, int size, boolean validated) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.data = data;
        this.course = course;
        this.size = size;
        this.validated = validated;
    }

    public Document(int user_id, String name, String description, String course, int size, boolean validated) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.course = course;
        this.size = size;
        this.validated = validated;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getValidated_admin() {
        return validated_admin;
    }

    public void setValidated_admin(int validated_admin) {
        this.validated_admin = validated_admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }


    public byte[] getData() {
        return data;
    }


    public void setData(byte[] data) {
        this.data = data;
    }

    
}

