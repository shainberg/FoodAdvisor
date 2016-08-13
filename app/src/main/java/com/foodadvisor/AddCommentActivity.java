package com.foodadvisor;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.core.AndroidSupport;
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

public class AddCommentActivity extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST = 1337;
    ImageView imageView;
    EditText editName;
    EditText editText;
    RatingBar ratingBar;
    Restaurant restaurant;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restaurant = (Restaurant) getIntent().getSerializableExtra("Restaurant");
        getSupportActionBar().setTitle(restaurant.getName());
        editName = (EditText) findViewById(R.id.editName);
        editText = (EditText) findViewById(R.id.editText);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        ImageButton galleryButton = (ImageButton) findViewById(R.id.gallery_button);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(openGallery , 0);
            }
        });

        imageView = (ImageView) findViewById(R.id.add_comment_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //imageurl = getRealPathFromURI(imageUri);

//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            textView.setText(imageBitmap.getHeight() + " " + imageBitmap.getWidth());
//            imageView.setImageBitmap(imageBitmap);
        }
        else if (requestCode == 0 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    public void saveComment(View view){
        Comment comment = new Comment(editName.getText().toString(), editText.getText().toString(), ratingBar.getRating(), restaurant.getId());

        final ProgressDialog progressDialog = new ProgressDialog(AddCommentActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        Model.instance().addComment(comment, new Model.AddCommentListener() {
            @Override
            public void done(String key) {
                System.out.println("comment successfully saved");

                uploadImage(key, new OnSuccessListener<String>(){
                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();
                        Toast.makeText(
                            MyApplication.getContext(), "Comment saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), RestaurantActivity.class);
                        intent.putExtra("Restaurant", restaurant);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public void uploadImage(String commentId, final OnSuccessListener successListener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://foodadvisor-c3bea.appspot.com");

        // Create a reference to 'images/mountains.jpg'
        StorageReference imageRef = storageRef.child("images/" + commentId + ".jpg");

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("image upload failed");
                successListener.onSuccess("failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("image successfully saved");
                successListener.onSuccess("success");
            }
        });
    }
}
