package com.foodadvisor.models;

import com.foodadvisor.DAL.DBHandler;
import com.foodadvisor.MainActivity;
import com.foodadvisor.MyApplication;

import java.util.List;

/**
 * Created by Ori on 07/08/2016.
 */

public class Model {
    private final static Model instance = new Model();
    DBHandler dbHandler;

    private Model() {
        dbHandler = new DBHandler(MyApplication.getContext());
    }

    public static Model instance() {
        return instance;
    }

    public interface GetCommentsListener {
        void done(List<Comment> stList);
    }

    public void getComments(Integer restaurantId, GetCommentsListener listener) {
        dbHandler.getComments(restaurantId, listener);
    }

    public interface GetRestaurantsListener {
        void done(List<Restaurant> stList);
    }

    public void getRestaurants(GetRestaurantsListener listener) {
        dbHandler.getRestaurants(listener);
    }

    public interface GetRestaurantListener {
        void done(Restaurant rest);
    }

    public void getRestaurant(Integer restaurantId, GetRestaurantListener listener) {
        dbHandler.getRestaurant(restaurantId, listener);
    }

    public interface AddCommentListener {
        void done();
    }

    public void addComment(Comment comment, AddCommentListener listener) {
        dbHandler.addComment(comment, listener);
    }
}

