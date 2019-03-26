package com.photograph_u.domain;

import java.util.List;

public class Photographer {
    private int id;
    private User user;
    private String cardNo;
    private String cardImage;
    private double price;
    private String serverContent;
    private String introduce;
    private int starValue;
    private List<Photo> photos;

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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServerContent() {
        return serverContent;
    }

    public void setServerContent(String serverContent) {
        this.serverContent = serverContent;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getStarValue() {
        return starValue;
    }

    public void setStarValue(int starValue) {
        this.starValue = starValue;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
