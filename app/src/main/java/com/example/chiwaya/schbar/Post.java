
package com.example.chiwaya.schbar;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Post extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    private static final int CAMERA_REQUEST = 0;
    private Uri uri ;
    private ImageView imageview;
    private EditText captionPost;
    private EditText captioNotes;
    private StorageReference storageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String downloadUrl;
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private String firestoreUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        captionPost = findViewById(R.id.captionTitle);
        captioNotes = findViewById(R.id.captionText);
        imageview = findViewById(R.id.postImage);

        storageReference = FirebaseStorage.getInstance().getReference("SCHBar/UDC/");

        firestore.collection("Users").whereEqualTo("Email",firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document : task.getResult())
                    {
                       firestoreUser = document.getData().get("User").toString();
                    }
                }

            }
        });
    }

    //###############################################################################################################################
    //*****************************************************  Work on this  **********************************************************
    //###############################################################################################################################

     public void camera(View view) {
         try {
             if (ActivityCompat.checkSelfPermission(Post.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(Post.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
             } else {
                 Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(takePicture, CAMERA_REQUEST);
             }
         }
         catch (Exception e) {
             e.printStackTrace();
         }
     }

     public void gallery(View view) {
         try {
             if (ActivityCompat.checkSelfPermission(Post.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(Post.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
             } else {
                 Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(pickPhoto , PICK_FROM_GALLERY);
             }
         }
         catch (Exception e) {
             e.printStackTrace();
         }
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            }
            case CAMERA_REQUEST:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, CAMERA_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
            }
        }
    }
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
         super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
         switch(requestCode) {
             case CAMERA_REQUEST:
                 if(resultCode == RESULT_OK){
                     Uri selectedImage = imageReturnedIntent.getData();
                     imageview.setImageURI(selectedImage);
                     uri = selectedImage;
                 }
                 break;
             case PICK_FROM_GALLERY:
                 if(resultCode == RESULT_OK){
                     Uri selectedImage = imageReturnedIntent.getData();
                     imageview.setImageURI(selectedImage);
                     uri = selectedImage;
                 }
                 break;
         }
     }
     /*
     * This is the segment of code where I programmed how the imageUri and accompanying data is stored online and on the local database.
     * */
    public void createNew_Posting(View view) {

        //creat a safe check for cases where there is no imageUri attached
        final String caption = captionPost.getText().toString().trim();
        final String notes = captioNotes.getText().toString();
        final String user = firestoreUser;

        if(uri == null){
                Toast.makeText(this, "Image was not uploaded, please spick imageUri again",Toast.LENGTH_SHORT).show();
        }else{
                final StorageReference store = storageReference.child(user).child("Posts")
                        .child(uri.getLastPathSegment());
                final UploadTask uploadTask;
                uploadTask = store.putFile(uri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return store.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            write_New_Data(user,downloadUri.toString(),caption,notes);
                        }
                    }
                });
        }
    }

    private void write_New_Data(String user, String uri, String title, String description ){

        Map<String, Object> postItem = new HashMap<>();

        postItem.put("user",user);
        postItem.put("imageUri",uri);
        postItem.put("title",title);
        postItem.put("description",description);

        firestore.collection("Posts").add(postItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Post.this,"The post has been made",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Post.this, mainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Post.this,"The post failed",Toast.LENGTH_LONG).show();
            }
        });
    }

}
