
package com.example.chiwaya.schbar;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Post extends AppCompatActivity {

    private int GALLERY_REQUEST = 2;
    private Uri uri = null;
    final File file = new File( DOWNLOAD_SERVICE, "https://www.bing.com/images/search?view=detailV2&ccid=FWXXmz8i&id=8E7ACA133E32CA67435216BB3D90AF1E9264D44F&thid=OIP.FWXXmz8ih2xj_BEKu6-xhAHaEK&mediaurl=http%3a%2f%2fwww.dreadcentral.com%2fwp-content%2fuploads%2f2017%2f06%2frick-and-morty.jpg&exph=1080&expw=1920&q=rick+and+morty&simid=608017279566086681&selectedIndex=9");
    private Uri newUri =  Uri.fromFile(file);
    private ImageView imgbutton;
    private EditText captionPost;
    private EditText captioNotes;
    private StorageReference storageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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

        if(uri == null){
                uri = newUri;
        }else{
                storageReference.child("UDC_URIs").child(user.getUid()).child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri completedUri = taskSnapshot.getUploadSessionUri();
                        newUri = completedUri;
                    }
                });
            }

        wrtie_New_Data(user.getUid(),newUri.toString(),caption,notes);
    }

    private void wrtie_New_Data(String user, String uri, String title, String description ){

        Map<String, Object> postItem = new HashMap<>();

        postItem.put("User",user);
        postItem.put("Image_Uri",uri);
        postItem.put("Title",title);
        postItem.put("Description",description);

        firestore.collection("SCHBar").document("UDC").collection("Posts").add(postItem)
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
