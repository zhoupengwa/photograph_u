package com.photograph_u.domain;

public class Comment {
    private int id;
    private User user;
    private Note note;
    private String content;
    private Comment father;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment getFather() {
        return father;
    }

    public void setFather(Comment father) {
        this.father = father;
    }
}
