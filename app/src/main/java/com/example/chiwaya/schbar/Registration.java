package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText username, email, password, passwordConfirm;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.register_login);
        username = findViewById(R.id.register_user);
        password = findViewById(R.id.register_passcode);
        passwordConfirm = findViewById(R.id.register_passcode_confirm);
    }

    public void Final_Register(View view) {

        String login = email.getText().toString();
        String pass  = password.getText().toString();
        String userID = username.getText().toString();
        String passConf  = passwordConfirm.getText().toString();

        if(TextUtils.isEmpty(login))
        {
            toast.makeText(this, R.string.email_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            toast.makeText(this, R.string.password_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passConf))
        {
            toast.makeText(this, R.string.confirmation_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userID))
        {
            toast.makeText(this, R.string.user_not_match, Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.equals(pass,passConf))
        {
            Toast.makeText(this, R.string.pasword_not_match, Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(login,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            toast.makeText(Registration.this, R.string.user_created,toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this, Choice.class));
                            return;

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
