package com.example.chiwaya.schbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    public  Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.user_login);
        password = (EditText) findViewById(R.id.user_passcode);
    }

    public void register_Account(View view) {

        startActivity(new Intent(Login.this ,Registration.class));
    }

    public void Login_Account(View view) {

        String login = email.getText().toString();
        String pass  = password.getText().toString();

        if(TextUtils.isEmpty(login))
        {
            toast.makeText(this, R.string.wrong_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass))
        {
            toast.makeText(this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(login,pass);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        startActivity(new Intent( Login.this, Choice.class));
        toast.makeText(this, R.string.failed_login, Toast.LENGTH_SHORT).show();
        return;
    }
}
