package com.evoxlk.sliitgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText name, email, password;
    Button signin, signup;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);


        progressBar  = findViewById(R.id.progressBar);
        fAuth =  FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(Register.this, MainInterface.class));
            finish();
        } else {

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String userName = name.getText().toString().trim();
                    final String userEmail = email.getText().toString().trim();
                    String userPassword = password.getText().toString().trim();


                    if (TextUtils.isEmpty(userEmail)) {
                        email.setError("Email is required...!");
                        return;
                    } else if (TextUtils.isEmpty(userName)) {
                        email.setError("Name is required...!");
                        return;
                    } else if (TextUtils.isEmpty(userPassword)) {
                        email.setError("Password is required...!");
                        return;
                    }


                    progressBar.setVisibility(View.VISIBLE);

                    //reg new user

                    fAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                //send verification email

                                FirebaseUser user = fAuth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Successfully registered, please verify your email", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Sending verification email is failed " + e.getMessage());
                                    }
                                });


                                userId = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("students").document(userId);

                                Map<String, Object> student = new HashMap<>();
                                student.put("studentName", userName);
                                student.put("studentEmail", userEmail);

                                documentReference.set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "User profile is saved on fireStore : " + userId);
                                    }
                                });

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), Login.class));

                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            });


        }




    }
}