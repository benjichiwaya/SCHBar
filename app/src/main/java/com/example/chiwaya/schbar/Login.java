package com.example.chiwaya.schbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    private TextView textView;
    private ImageView imageView;
    private CheckBox checkBox;
    private String pass,login;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.user_login);
        password = findViewById(R.id.user_passcode);
        textView = findViewById(R.id.text_register);
        imageView = findViewById(R.id.imageView);
        checkBox = findViewById(R.id.checkBox);

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences("Login_Data", Context.MODE_PRIVATE);
        login = sharedPreferences.getString("User","Def");
        pass = sharedPreferences.getString("Passcode","Def");

        firebaseAuth.signInWithEmailAndPassword(login, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, R.string.login_saved,
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, Choice.class));
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });

    }


    public void testEmail(boolean test)
    {
        if(test){
            email.setError("Please Enter Valid Email");
        }
    }

    public void register_Account(View view) {

        startActivity(new Intent(Login.this ,Registration.class));
    }

    public void Login_Account(View view) {


        if (login.isEmpty()||pass.isEmpty()){
            Toast.makeText(Login.this, R.string.one_entry_missing,
                    Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                startActivity(new Intent(Login.this, Choice.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {

                    }
                }
            });
        }
    }
}
