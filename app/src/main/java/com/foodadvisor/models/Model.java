package com.foodadvisor.models;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.foodadvisor.DAL.DBHandler;
import com.foodadvisor.MainActivity;
import com.foodadvisor.MyApplication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
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
        void done(String key);
    }

    public void addComment(Comment comment, AddCommentListener listener) {
        dbHandler.addComment(comment, listener);
    }

    public void saveImage(final String imageName, final Bitmap imageBitmap, final OnSuccessListener listener) {
        saveImageToFile(imageBitmap,imageName + ".jpg"); // synchronously save image locally
        dbHandler.uploadImage(imageName, imageBitmap, listener);
    }

    public interface LoadImageListener{
        public void onResult(List<Comment> imageBmp);
    }

    public void loadImages(final List<Comment> comments, final LoadImageListener listener) {
        for (final Comment comment : comments) {
            final String imageName = comment.getId() + ".jpg";
            Uri uri = loadImageUriFromFile(imageName);
            if (uri != null){
                comment.setImageUri(uri);
            }else{
                dbHandler.loadImageByBytes(comment, new OnSuccessListener<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if (bitmap != null){
                            saveImageToFile(bitmap, imageName);    //save the image locally for next time
                            bitmap.recycle();
                            bitmap = null;
                            comment.setImageUri(loadImageUriFromFile(imageName));
                        }
                    }
                });
            }
        }

        listener.onResult(comments);
    }

    private Uri loadImageUriFromFile(String imageFileName){

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(dir,imageFileName);

        if (!imageFile.exists()){
            return null;
        }

        Uri uri = Uri.fromFile(imageFile);
        Log.d("tag","got image from cache: " + imageFileName);
        return uri;
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            MyApplication.getContext().sendBroadcast(mediaScanIntent);
            Log.d("tag","add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private Bitmap loadImageFromFile(String imageFileName){
//        String str = null;
//        Bitmap bitmap = null;
//        try {
//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File imageFile = new File(dir,imageFileName);
//            //File dir = context.getExternalFilesDir(null);
//            InputStream inputStream = new FileInputStream(imageFile);
//            bitmap = BitmapFactory.decodeStream(inputStream);
//            Log.d("tag","got image from cache: " + imageFileName);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }

    //
//    private void saveImageUriToFile(Uri imageUri, String imageFileName){
//
//        InputStream is;
//
//        System.out.println("saving to file.... " + imageUri.getPath());
//        try {
//            File dir = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            File imageFile = new File(dir,imageFileName);
//            imageFile.createNewFile();
//
//            URL url = new URL(imageUri.toString());
//            /* Open a connection to that URL. */
//            URLConnection ucon = url.openConnection();
//
//            /*
//             * Define InputStreams to read from the URLConnection.
//             */
//            is = ucon.getInputStream();
//
//            OutputStream os = new FileOutputStream(imageFile);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//
////            final int chunkSize = 1024;  // We'll read in one kB at a time
////            byte[] imageData = new byte[chunkSize];
////            InputStream in = MyApplication.getContext().getContentResolver().openInputStream(uri1);
////            OutputStream out = new FileOutputStream(imageFile);  // I'm assuming you already have the File object for where you're writing to
////
////            try {
////                int bytesRead;
////                while ((bytesRead = in.read(imageData)) > 0) {
////                    out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
////                }
////
////            } catch (Exception ex) {
////                Log.e("Something went wrong.", ex.getMessage());
////            } finally {
////                in.close();
////                out.close();
////            }
//
//            //add the picture to the gallery so we dont need to manage the cache size
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri contentUri = Uri.fromFile(imageFile);
//            mediaScanIntent.setData(contentUri);
//            MyApplication.getContext().sendBroadcast(mediaScanIntent);
//            Log.d("tag","add image to cache: " + imageFileName);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

