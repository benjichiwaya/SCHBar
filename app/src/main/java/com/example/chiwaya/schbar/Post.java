
package com.example.chiwaya.schbar;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    private int GALLERY_REQUEST = 2;
    private Uri uri ;
    final File file = new File( DOWNLOAD_SERVICE, "https://firebasestorage.googleapis.com/v0/b/checkit-db.appspot.com/o/" +
                                                        "SCHBar%2FUDC%2FUDC_URIs%2FRdG0KjiMdCPqaUj0Dvo4cu6Y40H2%2F18717?alt=media&token=74a62205-2dfc-4f44-88f8-202837aa7454");
    private Uri newUri =  Uri.fromFile(file);
    private ImageView imgbutton;
    private EditText captionPost;
    private EditText captioNotes;
    private StorageReference storageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String user_DB_name = "User Unkown";
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        captionPost = findViewById(R.id.captionTitle);
        captioNotes = findViewById(R.id.captionText);


        storageReference = FirebaseStorage.getInstance().getReference("SCHBar/UDC/");
    }

    public void addNewPhoto(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Post_Images");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent,GALLERY_REQUEST);
    }

    //###############################################################################################################################
    //*****************************************************  Work on this  **********************************************************
    //###############################################################################################################################
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GALLERY_REQUEST && requestCode == Activity.RESULT_OK) {
             uri = data.getData();
             imgbutton = findViewById(R.id.postImage);
             imgbutton.setImageURI(uri);

             ParcelFileDescriptor fd;
             try {
                 fd = getContentResolver().openFileDescriptor(data.getData(), "r");
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
                 return;
             }
             Bitmap bmp = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
         } else {

             uri = data.getData();
             imgbutton = findViewById(R.id.postImage);
             imgbutton.setImageURI(uri);
         }
     }
     /*
     * This is the segment of code where I programmed how the image and accompanying data is stored online and on the local database.
     * */
    public void createNew_Posting(View view) {

        //creat a safe check for cases where there is no image attached
        final String caption = captionPost.getText().toString().trim();
        final String notes = captioNotes.getText().toString();
        final String user = firestore.collection("Users").whereEqualTo("User", firebaseUser.getUid()).toString();

        if(uri == null){
                uri = newUri;
        }else{
                storageReference.child("UDC_URIs").child(user).child(uri.getLastPathSegment()).putFile(uri)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                  uri = taskSnapshot.getUploadSessionUri();
                                write_New_Data(user,newUri.toString(),caption,notes);
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
