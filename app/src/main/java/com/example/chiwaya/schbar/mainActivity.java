package com.example.chiwaya.schbar;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class mainActivity extends AppCompatActivity     implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAG = "mainActivity";
    private Context postContext = this;
    private FirestoreRecyclerAdapter<PostItem,SCHBarViewHolder> firebaseAdapter;
    private Query query;
    private RecyclerView mainList;
    private String uri;
    private String User;
    private String Title;
    private String Notes;
    private FloatingActionButton fab;
    NavigationView navigationView;

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

        @SuppressLint("WrongViewCast")
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainList = findViewById(R.id.mainRecyclerList);
        mainList.setHasFixedSize(false);
        mainList.setLayoutManager(new LinearLayoutManager(postContext));
        firestore.collection("SCHBar").document("UDC").collection("Posts").limit(10);

        loadData();
        Log.d(TAG, "onCreate: This worked *******************************************************************************");
        setUpAdapter();

        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat appOptions = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(mainActivity.this, fab, ViewCompat.getTransitionName(fab));

                startActivity( new Intent(mainActivity.this,Post.class), appOptions.toBundle());
            }
        });
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
                   protected void onBindViewHolder(@NonNull final SCHBarViewHolder holder, final int position, @NonNull PostItem model) {

                        Log.d(TAG, "onBindViewHolder: called");
                        holder.setView(model.getUser(), model.getImageUri(), model.getTitle(), model.getDescription());
                        User = model.getUser();
                        uri = model.getImageUri();
                        Title = model.getTitle();
                        Notes = model.getDescription();

                        //holder.itemView.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { }});

                   }
                   @NonNull
                   @Override
                   public SCHBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

                       Context context = parent.getContext();
                       LayoutInflater inflater = LayoutInflater.from(context);

                       View myView = inflater.inflate(R.layout.main_row, parent, false);
                      final SCHBarViewHolder viewHolder = new SCHBarViewHolder(myView);
                       viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent = new Intent(mainActivity.this,Details.class);
                               ActivityOptionsCompat appOptions1 = ActivityOptionsCompat
                                       .makeSceneTransitionAnimation(mainActivity.this, viewHolder.Image, ViewCompat.getTransitionName(viewHolder.Image));
                               intent.putExtra("User",User);
                               intent.putExtra("Image",uri);
                               intent.putExtra("Title",Title);
                               intent.putExtra("Description",Notes);

                               startActivity(intent,appOptions1.toBundle());
                           }
                       });
                       return viewHolder;
                   }
               };
               mainList.setAdapter(firebaseAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_gallery)
        {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    findViewById(R.id.imageView_header).setBackgroundResource(R.drawable.gradient1);
                    findViewById(R.id.mainActivity).setBackgroundResource(R.drawable.gradient1);
                    return true ;
                }
            });

        } else if (id == R.id.nav_slideshow) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    findViewById(R.id.imageView_header).setBackgroundResource(R.drawable.gradient2);
                    findViewById(R.id.mainActivity).setBackgroundResource(R.drawable.gradient2);
                    return true;
                }
            });


        } else if (id == R.id.nav_manage) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Application application = new Application();
                    application.onTerminate();
                    return true;
                }
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

