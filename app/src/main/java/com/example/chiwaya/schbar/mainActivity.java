package com.example.chiwaya.schbar;

import android.app.DownloadManager;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class mainActivity extends AppCompatActivity {

    private RecyclerView mainList;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAG = "mainActivity";
    private Context postContext = this;
    private Query query;
    private FirestoreRecyclerAdapter<PostItem,SCHBarViewHolder> firebaseAdapter;

/*
* Dont forget to addd Key = getKey(), its important for indexing
* */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onCreate: This is the first failure");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started with no errors");
         mainList = findViewById(R.id.mainRecyclerList);
         mainList.setHasFixedSize(true);
         mainList.setLayoutManager(new LinearLayoutManager(postContext));

         loadData();

         setUpAdapter();
         mainList.setAdapter(firebaseAdapter);

    }

    private void loadData() {
        CollectionReference localDatabase = firestore.collection("SCHBar").document("UDC").collection("Posts");

        localDatabase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                   query = task.getResult().getQuery().limit(10);
                }
            }
        });
    }

    private void setUpAdapter ()
    {

       FirebaseRecyclerOptions<PostItem> options = new FirestoreRecyclerOptions.Builder<>().setQuery(query,PostItem.class).build();

        firebaseAdapter = new FirestoreRecyclerAdapter<PostItem, SCHBarViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull SCHBarViewHolder holder, int position, @NonNull PostItem model) {

                Log.d(TAG, "onBindViewHolder: called");

                holder.setView(mainActivity.this,model.getUser(),model.getImage(),model.getTitle(),model.getDesc());

                holder.view.findViewById(R.id.card_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mainActivity.this,"Something worked",Toast.LENGTH_SHORT).show();
                    }
                });

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


    }
}

