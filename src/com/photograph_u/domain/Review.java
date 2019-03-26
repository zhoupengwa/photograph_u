package com.photograph_u.domain;

public class Review {
    private int id;
    private User user;
    private Photographer phorographer;
    private String content;

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

    public Photographer getPhorographer() {
        return phorographer;
    }

    public void setPhorographer(Photographer phorographer) {
        this.phorographer = phorographer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
