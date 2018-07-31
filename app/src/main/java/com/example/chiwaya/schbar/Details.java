package com.example.chiwaya.schbar;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView user;
    private ImageView imageView;

    private String USER;
    private String TITLE;
    private String DESCRIPTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        user = findViewById(R.id.user_2);
        imageView = findViewById(R.id.final_image2);
        title = findViewById(R.id.post_descript_2);
        description = findViewById(R.id.post_notes_2);

        user.setText(intent.getStringExtra("User"));
        Picasso.get().load(intent.getStringExtra("Image")).into(imageView);
        title.setText(intent.getStringExtra("Title"));
        description.setText(intent.getStringExtra("Description"));

    }

    public void Publish(View view) {

        startActivity( new Intent(Details.this,Post.class));
    }

    public void openChats(View view) {
        startActivity(new Intent(Details.this,Chats.class));
    }
}
