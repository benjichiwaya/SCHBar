package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private EditText username, email, password, passwordConfirm;
    public Toast toast;
    private FirebaseUser User;
    private FirebaseFirestore firestore;
    private String login;
    private String pass;
    private String userName;
    private String passConf;
    private static final String TAG = Registration.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firestore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.register_login);
        username = findViewById(R.id.register_user);
        password = findViewById(R.id.register_passcode);
        passwordConfirm = findViewById(R.id.register_passcode_confirm);

        password.addTextChangedListener(new TextWatcher() {
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
    }

    public void Final_Register(View view) {

        if (password.getText() != passwordConfirm.getText()) {
            passwordConfirm.setError("Passwords do not match");
        }
        login = email.getText().toString();
        pass = password.getText().toString();
        userName = username.getText().toString();
        passConf = passwordConfirm.getText().toString();

        if (!(login.isEmpty() || pass.isEmpty() || userName.isEmpty() || passConf.isEmpty())) {
            firebaseAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                toast.makeText(Registration.this, R.string.user_created, toast.LENGTH_SHORT).show();
                                creat_User_Auth(login, pass);
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
                Toast.makeText(this, R.string.one_entry_missing, Toast.LENGTH_SHORT).show();
            }

    }

    private void creat_User_Auth(String login,  String pass)
    {
        Map<String, Object> user = new HashMap<>();

        user.put("User", userName);
        user.put("Email", login);
         firestore.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            startActivity(new Intent(Registration.this, Choice.class));
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            toast.makeText(Registration.this, R.string.auth_failed, toast.LENGTH_SHORT).show();
            Log.w(TAG,"It failed here because of: ",e);
        }
    });

    }

}
