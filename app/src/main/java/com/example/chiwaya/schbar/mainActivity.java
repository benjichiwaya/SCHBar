package com.example.chiwaya.schbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class mainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private DatabaseReference localDatabase;
    private FirebaseDatabase fireDatabase;

    private static final String TAG = "mainActivity";

    //Variables used
    private ArrayList<String> newUser   = new ArrayList<>();
    private ArrayList<String> newTitle  = new ArrayList<>();
    private ArrayList<String> newDscrpt = new ArrayList<>();
    private ArrayList<String> newImage  = new ArrayList<>();
    private Context postContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started with no errors");

         mainList = findViewById(R.id.mainRecyclerList);
         mainList.setHasFixedSize(true );
         mainList.setLayoutManager( new LinearLayoutManager(this));
    }

    Query query = FirebaseDatabase.getInstance()
        .getReference().child("post").limitToLast(10);

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
                        Log.d(TAG, "onBindViewHolder: called");
                        //loading all images
                        Glide.with(postContext)
                                .asBitmap()
                                .load(newImage.get(position)).into(holder.image);
                        //Loading all text information
                        holder.title.setText(newTitle.get(position));
                        holder.descrptn.setText(newDscrpt.get(position));
                        holder.user.setText(newUser.get(position));

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
            /*
            * **************************************
            *                                      *
            *                                      *
            *        This code is crutial          *
            *                                      *
            *                                      *
            * **************************************
            *
            * It defines what happens when the item (card view and all of it's contents) are clicked
            * This is where you add the code for the new page.
            * */
                            }
                        });
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

        ImageView image;
        CardView cardView;
        TextView title, user, descrptn;

        public MainViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.final_image);
                cardView = itemView.findViewById(R.id.cardView_parent);
                title = itemView.findViewById(R.id.post_descript);
                descrptn = itemView.findViewById(R.id.post_notes);
                user = itemView.findViewById(R.id.card_user);
        }
    }
}

