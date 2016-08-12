package com.foodadvisor.DAL;

import android.content.Context;
import android.content.SyncStatusObserver;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.foodadvisor.models.Comment;
import com.foodadvisor.models.Model;
import com.foodadvisor.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OR on 06/05/2016.
 */
public class DBHandler {
    public DBHandler(Context context) {
        Firebase.setAndroidContext(context);
    }

    public void getRestaurants(final Model.GetRestaurantsListener listener) {
        Firebase ref = new Firebase("https://foodadvisor-c3bea.firebaseio.com/restaurants");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("fetching rests");

                final List<Restaurant> restaurants = new ArrayList<Restaurant>();

                if (snapshot.exists()) {

                    for (DataSnapshot child: snapshot.getChildren()) {
                        Restaurant restaurant = child.getValue(Restaurant.class);
                        restaurants.add(restaurant);
                        System.out.println("hi " + restaurant.getName());
                    }
                }
                else {
                    System.out.println("no restaurants");
                    //Toast toast = Toast.makeText(this, "email not found", Toast.LENGTH_SHORT);
                }

                listener.done(restaurants);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void getRestaurant(Integer restaurantId, final Model.GetRestaurantListener listener) {
        Firebase ref = new Firebase("https://foodadvisor-c3bea.firebaseio.com/restaurants");
        Query queryRef = ref.orderByChild("id").equalTo(restaurantId);

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("fetching rest");

                if (snapshot.exists()) {
                    for (DataSnapshot child: snapshot.getChildren()) {
                        Restaurant restaurant = child.getValue(Restaurant.class);
                        System.out.println("hi " + restaurant.getName());
                        listener.done(restaurant);
                        break;
                    }
                }
                else {
                    System.out.println("no restaurants");
                    //Toast toast = Toast.makeText(this, "email not found", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void getComments(Integer restaurantId, final Model.GetCommentsListener listener) {
        Firebase ref = new Firebase("https://foodadvisor-c3bea.firebaseio.com/comments");
        Query queryRef = ref.orderByChild("restaurantId").equalTo(restaurantId);

        queryRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot snapshot) {
                 System.out.println("fetching comments");

                 final List<Comment> comments = new ArrayList<Comment>();

                 if (snapshot.exists()) {
                     for (DataSnapshot child: snapshot.getChildren()) {
                         Comment comment = child.getValue(Comment.class);
                         comments.add(comment);
                         System.out.println("hi " + comment.getName());
                     }
                 }
                 else {
                     System.out.println("no comments");
                     //Toast toast = Toast.makeText(this, "email not found", Toast.LENGTH_SHORT);
                 }

                 listener.done(comments);
             }

             @Override
             public void onCancelled(FirebaseError error) {
             }
        });
    }

    public void addComment(Comment comment, final Model.AddCommentListener listener) {
        Firebase ref = new Firebase("https://foodadvisor-c3bea.firebaseio.com/comments");

        final Firebase newCommentRef = ref.push();
        newCommentRef.setValue(comment, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                    listener.done(newCommentRef.getKey());
                }
            }
        });
    }

}


