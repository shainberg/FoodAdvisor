package com.foodadvisor.DAL;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.foodadvisor.MyApplication;
import com.foodadvisor.models.Comment;
import com.foodadvisor.models.Model;
import com.foodadvisor.models.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OR on 06/05/2016.
 */
public class DBHandler {
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://foodadvisor-c3bea.appspot.com");

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

//        final FirebaseStorage storage = FirebaseStorage.getInstance();
//        // Create a storage reference from our app
//        final StorageReference storageRef = storage.getReferenceFromUrl("gs://foodadvisor-c3bea.appspot.com");
        queryRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot snapshot) {
                 System.out.println("fetching comments");
                 final List<Comment> comments = new ArrayList<Comment>();

                 if (snapshot.exists()) {
//                     final Counter counter = new Counter((int) snapshot.getChildrenCount());

                     for (DataSnapshot child: snapshot.getChildren()) {
                         final Comment comment = child.getValue(Comment.class);

                         comment.setId(child.getKey());
                         comments.add(comment);
                     }
                 }
                 else {
                     System.out.println("no comments");
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

    public void uploadImage(String commentId, Bitmap bitmap, final OnSuccessListener successListener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://foodadvisor-c3bea.appspot.com");

        // Create a reference to 'images/mountains.jpg'
        StorageReference imageRef = storageRef.child("images/" + commentId + ".jpg");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imageByteArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("image upload failed");
                successListener.onSuccess("without image");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("image successfully saved");
                successListener.onSuccess("");
            }
        });
    }

    public void loadImage(final Comment comment, final OnSuccessListener<Uri> listener){
        storageRef.child("images/" + comment.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri data) {
                System.out.println("load image: " + comment.getId());
                comment.setImageUri(data);
                listener.onSuccess(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void loadImageByBytes(final Comment comment, final OnSuccessListener<Bitmap> listener){
        storageRef.child("images/" + comment.getId() + ".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] data) {
                System.out.println("load image: " + comment.getId());
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getContext().getContentResolver(), uri);

                Bitmap bmp;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                //comment.setImage(bmp);
                listener.onSuccess(bmp);
                System.out.println("load image: " + comment.getId() +" finished");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private class Counter {
        public Integer count = 0;
        public Integer limit;


        public Counter(Integer limit){
            this.limit = limit;
        }

        public Boolean Up(){
            count++;
            return (count == limit);
        }
    }
}


