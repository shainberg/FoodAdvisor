package com.foodadvisor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;
import com.foodadvisor.DAL.DBHandler;
import com.foodadvisor.models.Comment;
import com.foodadvisor.models.Model;
import com.foodadvisor.models.Restaurant;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Model.instance().addComment(new Comment(3, "shainby", "מאוד בסדר גמור", "", 5, "05/06/1993 04:10", 2), new Model.AddCommentListener() {
//            @Override
//            public void done() {
//
//            }
//        });

//        Model.instance().getRestaurants(new Model.GetRestaurantsListener() {
//            @Override
//            public void done(List<Restaurant> stList) {
//                System.out.println("shalom " + stList.size());
//            }
//        });

//        Model.instance().getRestaurant(2, new Model.GetRestaurantListener() {
//            @Override
//            public void done(Restaurant rest) {
//                System.out.println("shalom " + rest.getName());
//            }
//        });

//        Model.instance().getComments(1, new Model.GetCommentsListener() {
//            @Override
//            public void done(List<Comment> stList) {
//                System.out.println("shalom " + stList.size());
//            }
//        });

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void onStart(){
        super.onStart();

    }

}
