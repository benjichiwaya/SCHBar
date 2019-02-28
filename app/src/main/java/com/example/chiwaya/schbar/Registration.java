package com.example.chiwaya.schbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private EditText username, email, password, passwordConfirm;
    private FirebaseFirestore firestore;
    private String login;
    private String pass;
    private String userName;
    private String passConf;
    private String d_p_url;

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
    }

    public void Final_Register(View view) {
        login = email.getText().toString();
        pass = password.getText().toString();
        userName = username.getText().toString();
        passConf = passwordConfirm.getText().toString();
        d_p_url = " ";
        final Map <String,Object> User = new HashMap<>();

        if (!(login.isEmpty() || pass.isEmpty() || userName.isEmpty() || passConf.isEmpty())) {
            firebaseAuth.createUserWithEmailAndPassword(login, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        //Check if d_p_ur is empty
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                User.put("User", userName);
                                User.put("Email",login);
                                User.put("Profile_Picture", d_p_url);
                                firestore.collection("Users").add(User).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(Registration.this, R.string.user_created, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this, Login.class));
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Registration.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
                Toast.makeText(this, R.string.one_entry_missing, Toast.LENGTH_SHORT).show();
            }

    }

    public void register_image(View view) {

        //Work on code to select image for profile picture
    }
}
