package com.foodadvisor.models;

import android.util.TimeUtils;

import java.util.Date;

/**
 * Created by OR on 06/05/2016.
 */
public class Comment {
    Integer id;
    String name;
    String content;
    String image;
    Integer rate;
    String date;
    Integer restaurantId;

    public Comment() {
    }

    public Comment(Integer id, String name, String content, String image, Integer rate, String date, Integer restaurantId) {
        this.id = id;
        this.name= name;
        this.content = content;
        this.image = image;
        this.rate = rate;
        this.date = date;
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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

}
