package com.example.chiwaya.schbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class mainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAG = "mainActivity";
    private Context postContext = this;
    private FirestoreRecyclerAdapter<PostItem,SCHBarViewHolder> firebaseAdapter;
    private Query query;
    private RecyclerView mainList;

/*
* Dont forget to addd Key = getKey(), its important for indexing
* */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onCreate: This is the first failure");

        firebaseAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started with no errors ******************************************************************************");
        mainList = findViewById(R.id.mainRecyclerList);
        mainList.setHasFixedSize(false);
        mainList.setLayoutManager(new LinearLayoutManager(postContext));
        firestore.collection("SCHBar").document("UDC").collection("Posts").limit(10);

         loadData();
        Log.d(TAG, "onCreate: This worked *******************************************************************************");
        setUpAdapter();

    }

    private void loadData() {
        query = firestore.collection("Posts").limit(30);
    }

    private void setUpAdapter ()
    {

        FirestoreRecyclerOptions<PostItem> options = new FirestoreRecyclerOptions
               .Builder<PostItem>().setQuery(query,PostItem.class).build();

               firebaseAdapter = new FirestoreRecyclerAdapter<PostItem, SCHBarViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull SCHBarViewHolder holder, int position, @NonNull PostItem model) {

                       Log.d(TAG, "onBindViewHolder: called");

                       holder.setView(model.getUser(), model.getImageUri(), model.getTitle(), model.getDescription());

                   }

                   @NonNull
                   @Override
                   public SCHBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                       Context context = parent.getContext();
                       LayoutInflater inflater = LayoutInflater.from(context);

                       View myView = inflater.inflate(R.layout.main_row, parent, false);
                       SCHBarViewHolder viewHolder = new SCHBarViewHolder(myView);
                       return viewHolder;
                   }
               };
        mainList.setAdapter(firebaseAdapter);
    }
}

