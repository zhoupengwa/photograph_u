package com.photograph_u.domain;

public class StyleInfo {
    private int id;
    private Photographer phorographer;
    private Style style;

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

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
