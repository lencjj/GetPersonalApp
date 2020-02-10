package com.example.personalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.backToMainBtn:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.changePasswordView:
                Intent j = new Intent(this, ChangePasswordActivity.class);
                startActivity(j);
                break;
            case R.id.editProfileView:
                Intent k = new Intent(this, EditProfileActivity.class);
                startActivity(k);
                break;
        }
    }
}
