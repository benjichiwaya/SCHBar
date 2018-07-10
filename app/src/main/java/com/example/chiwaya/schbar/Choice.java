package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Choice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
    }

    public void nerdClicked(View view) {
        startActivity( new Intent(Choice.this, Post.class));

    }

    public void groupeeCicked(View view) {
        startActivity( new Intent(Choice.this, mainActivity.class));
    }
}
