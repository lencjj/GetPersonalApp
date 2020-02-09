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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button updateBtn, resetBtn;
    ImageButton backBtn;
    EditText oldPassword, newPassword, confirmNewPassword;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        resetBtn = (Button) findViewById(R.id.changePassPageResetBtn);
        backBtn = (ImageButton) findViewById(R.id.backToSettingsBtn);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) findViewById(R.id.confirmNewPassword);
    }

    @Override
    public void onClick(View v) {
        if(v == updateBtn){
            updatePassword(oldPassword.getText().toString(), newPassword.getText().toString(), confirmNewPassword.getText().toString());
        }
        if(v == resetBtn){
            oldPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        }
        if(v == backBtn){
            Intent i = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
            startActivity(i);
        }
    }

    public void updatePassword(final String oldPass, final String newPass, final String confirmPass){
        if((TextUtils.isEmpty(oldPass)) || (TextUtils.isEmpty(newPass)) || (TextUtils.isEmpty(confirmPass))){
            //any empty fields
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Updating...");
        progressDialog.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(newPass.equals(oldPass)){
                        progressDialog.dismiss();
                        Toast.makeText(ChangePasswordActivity.this, "Old password cannot be the same as new password.", Toast.LENGTH_SHORT).show();
                    } else{
                        if(newPass.equals(confirmPass)){
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePasswordActivity.this, "Password successfully updated.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                                        startActivity(i);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePasswordActivity.this, "An error occured. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(ChangePasswordActivity.this, "New password does not match confirm password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else{
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
