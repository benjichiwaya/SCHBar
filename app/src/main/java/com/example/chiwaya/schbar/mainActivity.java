package com.example.chiwaya.schbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class mainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private DatabaseReference localDatabase;
    private FirebaseDatabase fireDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mainList = findViewById(R.id.mainRecyclerList);
         mainList.setHasFixedSize(true );
         mainList.setLayoutManager( new LinearLayoutManager(this));
    }

    Query query = FirebaseDatabase.getInstance()
        .getReference() .child("post") .limitToLast(10);

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            // ...
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            // ...
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            // ...
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // ...
        }
    };

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<PostItem> options = new FirebaseRecyclerOptions
                .Builder<PostItem>()
                .setQuery(query,PostItem.class)
                .build();

        FirebaseRecyclerAdapter <PostItem, MainViewHolder> fireRecycler =
                new FirebaseRecyclerAdapter<PostItem, MainViewHolder>(options){
                    @Override
                    protected void onBindViewHolder(@NonNull MainViewHolder holder, int position, @NonNull PostItem model) {
                    }
                    @NonNull
                    @Override
                    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            // Create a new instance of the ViewHolder, in this case we are using a custom
                            // layout called R.layout.message for each item
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.main_row, parent, false);
                            return new MainViewHolder(view);
                        }
                };
        mainList.setAdapter(fireRecycler);
    }

    public class  MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View itemView) {
            super(itemView);
            View view = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = findViewById(R.id.post_descript);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_note = findViewById(R.id.post_notes);
            post_note.setText(desc);
        }
    }
}

