package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText username, email, password, passwordConfirm;
    public Toast toast;
    public TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.register_login);
        username = findViewById(R.id.register_user);
        password = findViewById(R.id.register_passcode);
        passwordConfirm = findViewById(R.id.register_passcode_confirm);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!email.equals(Patterns.EMAIL_ADDRESS))
                {
                    email.setError("PLease Enter Valid Email");
                }
            }
        });
        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(password.getText() != passwordConfirm.getText())
                {
                    passwordConfirm.setError("Passwords do not match");
                }
                s.clear();
            }
        });
    }

    public void Final_Register(View view) {

        String login = email.getText().toString();
        String pass  = password.getText().toString();
        String userID = username.getText().toString();
        String passConf  = passwordConfirm.getText().toString();

        if(!(login.isEmpty()||pass.isEmpty()||userID.isEmpty()||passConf.isEmpty())) {
            firebaseAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                toast.makeText(Registration.this, R.string.user_created, toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registration.this, Choice.class));
                                return;
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Registration.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this,R.string.one_entry_missing,Toast.LENGTH_SHORT).show();
        }
    }
}
