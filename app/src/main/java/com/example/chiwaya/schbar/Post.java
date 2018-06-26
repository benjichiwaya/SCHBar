package com.example.chiwaya.schbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.net.URI;

public class Post extends AppCompatActivity {

    private int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private Uri newUri = null;
    private ImageView imgbutton;
    private EditText captionPost;
    private EditText captioNotes;
    private StorageReference storageReference;
    private FirebaseDatabase onlineDatabase;
    private DatabaseReference localDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        captionPost = findViewById(R.id.captionTitle);
        captioNotes = findViewById(R.id.captionText);
        storageReference = FirebaseStorage.getInstance().getReference();

        onlineDatabase = FirebaseDatabase.getInstance();
        localDatabase = onlineDatabase.getReference("");

        storageReference = FirebaseStorage.getInstance().getReference("UDC_URIs");
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


        storageReference.child("UDC_URIs").child(user.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri completedUri = taskSnapshot.getUploadSessionUri();
                newUri = completedUri;
                Toast.makeText(Post.this, R.string.upload, Toast.LENGTH_SHORT);

            }
        });

        wrtie_New_Data(user.getUid(),newUri.toString(),caption,notes);
        startActivity(new Intent(Post.this,Choice.class ));

    }

    private void wrtie_New_Data(String user, String uri, String title, String description ){

        PostItem postItem = new PostItem(uri,title,description);

        localDatabase.child("Post").child(user).setValue(postItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               startActivity( new Intent(Post.this, mainActivity.class));
            }
        });
    }
}
