package com.example.personalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginBtn;
    private Button redirectBtn;
    private EditText emailInput;
    private EditText passwordInput;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent j = new Intent(this, MainActivity.class);
            startActivity(j);

        }

        progressDialog = new ProgressDialog(this);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        redirectBtn = (Button) findViewById(R.id.redirectBtn);
        emailInput = (EditText) findViewById(R.id.logEmailInput);
        passwordInput = (EditText) findViewById(R.id.logPasswordInput);

    }

    private void userLogin(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

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
        //come here if valiations are ok
        progressDialog.setMessage("Loging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //user is successfully registered
                            finish();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Could not Login. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.redirectBtn:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.loginBtn:
                userLogin();
                break;

        }
    }
}
