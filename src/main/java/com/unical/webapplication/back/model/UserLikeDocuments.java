package com.unical.webapplication.back.model;

public class UserLikeDocuments {
private int user_id;
    private int document_id;

    public UserLikeDocuments(){}

    public UserLikeDocuments(int user_id, int document_id) {
        this.user_id = user_id;
        this.document_id = document_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getDocument_id() {
        return document_id;
    }
    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }
}
