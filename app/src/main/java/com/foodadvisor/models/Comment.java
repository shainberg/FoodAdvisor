package com.foodadvisor.models;

import java.util.Date;

/**
 * Created by ORI on 06/05/2016.
 */
public class Comment {
    Integer id;
    String name;
    String content;
    String image;
    Float rate;
    String date;
    Integer restaurantId;

    public Comment() {
    }

    public Comment(String name, String content, Float rate, Integer restaurantId) {
        this.name= name;
        this.content = content;
        this.rate = rate;
        this.date = new Date().toString();
        this.restaurantId = restaurantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

}
