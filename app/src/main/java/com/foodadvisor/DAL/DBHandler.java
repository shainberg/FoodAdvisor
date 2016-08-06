package com.foodadvisor.DAL;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.foodadvisor.models.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OR on 06/05/2016.
 */
public class DBHandler {
    private static DBHandler dbHandler;
    Firebase firebase;

    public DBHandler(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase("https://foodadvisor-c3bea.firebaseio.com/");
    }

    public static DBHandler getInstance() {
        if (dbHandler == null) {
            throw new NullPointerException("In first use you have to provide Context");
        }

        return dbHandler;
    }

    public static DBHandler getInstance(Context context) {
        if (dbHandler == null) {
            dbHandler = new DBHandler(context);
        }

        return dbHandler;
    }

    public void getComments(Integer restaurantId) {

        List<Comment> comments = null;
        System.out.println("KKK");
        firebase.child("comments").addValueEventListener(new ValueEventListener() {

                                                             @Override
                                                             public void onDataChange(DataSnapshot snapshot) {
                                                                         System.out.println("SHALOM");
                                                                         System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                                                                         //snapshot.getChildren(

                                                             }

                                                             @Override
                                                             public void onCancelled(FirebaseError error) {
                                                             }


                                                         }

        );
    }
}


