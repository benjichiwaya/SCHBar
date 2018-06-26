package com.example.chiwaya.schbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ImageView imgbutton;
    private EditText captionPost;
    private EditText captioNotes;
    private StorageReference storageReference;
    private FirebaseDatabase onlineData;
    private DatabaseReference localData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        captionPost = findViewById(R.id.captionTitle);
        captioNotes = findViewById(R.id.captionText);
        storageReference = FirebaseStorage.getInstance().getReference();
        onlineData.goOnline();

        //Remember to chack .getInstance omission. #does it affect the apps run.
        localData = onlineData.getReference().child("Post_Notes");



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

         if(requestCode == GALLERY_REQUEST && requestCode == Activity.RESULT_OK)
         {
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
             imgbutton.setImageBitmap(bmp);
         }
     }

    public void createNew_Posting(View view) {

        StorageReference filePath = storageReference.child("Image com.example.chiwaya.schbar.PostItem").child(uri.getLastPathSegment());

        //creat a safe check for cases where there is no image attached
        final String caption = captionPost.getText().toString().trim();
        final String notes = captioNotes.getText().toString();

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri completedUri = taskSnapshot.getUploadSessionUri();
                Toast.makeText(Post.this, R.string.upload, Toast.LENGTH_SHORT);

                DatabaseReference newPosting = localData.push();
                newPosting.child("caption").setValue(caption);
                newPosting.child("notes").setValue(notes);
                newPosting.child("attachment").setValue(completedUri.toString());

            }
        });
    }
}
