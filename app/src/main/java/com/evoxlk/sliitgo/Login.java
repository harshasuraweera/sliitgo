package com.evoxlk.sliitgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {


    EditText loginEmail, loginPassword;
    Button loginBtn, registerBtn;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;
    TextView forgotPassword, developerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn  = findViewById(R.id.loginBtn);
        fAuth =  FirebaseAuth.getInstance();
        forgotPassword  = findViewById(R.id.forgotPassword);
        progressBar2 = findViewById(R.id.progressBar2);

        developerDetails = findViewById(R.id.developerDetailss);

        developerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setMessage("App is designed and developed by Harsha Suraweera\nEmail : hi@harshasuraweera.me\nharshamanoj912@gmail.com" +
                        "\nWebsite: harshasuraweera.me");
                alertDialog.show();
            }
        });


        if (fAuth.getCurrentUser() != null){

            startActivity(new Intent(Login.this, MainInterface.class));
            finish();

        }else {


            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String userLoginEmail = loginEmail.getText().toString().trim();
                    String userLoginPassword = loginPassword.getText().toString().trim();


                    if (TextUtils.isEmpty(userLoginEmail)) {
                        loginEmail.setError("Email is required...!");
                        return;
                    } else if (TextUtils.isEmpty(userLoginPassword)) {
                        loginPassword.setError("Password is required...!");
                        return;
                    }


                    progressBar2.setVisibility(View.VISIBLE);

                    //login

                    fAuth.signInWithEmailAndPassword(userLoginEmail, userLoginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainInterface.class));
                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar2.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            });


            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final EditText resetMail = new EditText(view.getContext());


                    final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());

                    passwordResetDialog.setTitle("Want to reset password ?");
                    passwordResetDialog.setMessage("Enter your email here to get the reset link");
                    passwordResetDialog.setView(resetMail);


                    passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //send reset password email
                            String mail = resetMail.getText().toString();

                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Please check your mail box", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "There was an error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });


                    passwordResetDialog.create().show();

                }
            });



            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Register.class));
                }
            });


        }


    }
}