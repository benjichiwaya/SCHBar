package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Choice extends AppCompatActivity {

    private FirebaseFirestore firestore =  FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private String Login;
    private String UserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        creat_User_Database();
    }

    public void nerdClicked(View view) {
        startActivity( new Intent(Choice.this, Post.class));

    }

    public void groupeeCicked(View view) {
        startActivity( new Intent(Choice.this, mainActivity.class));
    }

    protected void creat_User_Database() {
        Map<String, Object> User = new HashMap<>();

        User.put("User", UserName);
        User.put("Email", Login);

        firestore.collection("Users").add(User).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                startActivity(new Intent(Choice.this, Choice.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Choice.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void sendUser_Info(String login, String userName) {
        this.Login = login;
        this.UserName = userName;
    }
}
