package com.example.chiwaya.schbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class mainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private DatabaseReference localDatabase;
    private FirebaseDatabase fireDatabase;
    private FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAG = "mainActivity";
    private Context postContext = this;
    private FirebaseRecyclerAdapter <PostItem,SCHBarViewHolder>firebaseAdapter;

/*
* Dont forget to addd Key = getKey(), its important for indexing
* */

    @Override
    protected void onStart() {
        super.onStart();
        setUpAdapter();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started with no errors");
         mainList = findViewById(R.id.mainRecyclerList);
         mainList.setHasFixedSize(true);
         mainList.setLayoutManager(new LinearLayoutManager(postContext));

         firestore.collection("Posts").get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult())
                            {
                                Log.d(TAG,"So far so goood");
                                //Research other ways of going about this. THis may crash once you load it.
                                localDatabase = (DatabaseReference) document.getData();
                            }
                        }
                     }
                 });


    }

    private void setUpAdapter ()
    {
        FirebaseRecyclerOptions<PostItem> options = new FirebaseRecyclerOptions.Builder<PostItem>()
                .setQuery(localDatabase,PostItem.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<PostItem, SCHBarViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull SCHBarViewHolder holder, int position, @NonNull PostItem model) {

                Log.d(TAG, "onBindViewHolder: called");

                holder.setView(mainActivity.this,model.user,model.image,model.title,model.desc);

            }
            @NonNull
            @Override
            public SCHBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        mainList.setAdapter(firebaseAdapter);

    }
}

