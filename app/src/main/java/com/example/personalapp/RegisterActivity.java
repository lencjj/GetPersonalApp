package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String KEY_IDENTIFIER = "identifier";
    private static final String KEY_USERNAME = "username";

    private Button registerBtn;
    private Button resetBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirm;
    private EditText username;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        registerBtn = (Button) findViewById(R.id.registerBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        username = (EditText) findViewById(R.id.usernameInput);
        emailInput = (EditText) findViewById(R.id.regEmailInput);
        passwordInput = (EditText) findViewById(R.id.regPasswordInput);
        passwordConfirm = (EditText) findViewById(R.id.regPasswordConfirm);




    }

    private void registerUser(){
        String username = this.username.getText().toString().trim();
        final String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confPassword = passwordConfirm.getText().toString().trim();

        final Map<String, Object> userRecord = new HashMap<>();
        userRecord.put(KEY_IDENTIFIER, email);
        userRecord.put(KEY_USERNAME, username);

        if(TextUtils.isEmpty(username)){
            //username empty
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password empty
            Toast.makeText( this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(confPassword)){
            //password empty
            Toast.makeText( this,"Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(password.equals(confPassword))){
            //passwords do not match
            Toast.makeText( this,"Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //come here if valiations are ok
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //user is successfully registered
                            db.collection("User information").document(email).set(userRecord);
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Could not Register. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if(view == registerBtn){
            registerUser();
        }
        if(view == resetBtn){
            username.setText("");
            emailInput.setText("");
            passwordInput.setText("");
            passwordConfirm.setText("");
        }
    }


}
