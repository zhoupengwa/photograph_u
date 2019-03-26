package com.photograph_u.domain;

public class Photo {
    private int id;
    private Photographer phorographer;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Photographer getPhorographer() {
        return phorographer;
    }

    public void setPhorographer(Photographer phorographer) {
        this.phorographer = phorographer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
