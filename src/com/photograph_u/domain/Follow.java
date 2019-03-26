package com.photograph_u.domain;

public class Follow {
    private int id;
    private User user;
    private Photographer phorographer;

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
}
