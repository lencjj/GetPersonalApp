package com.example.personalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CalendarWithEvent extends AppCompatActivity implements View.OnClickListener{

    CustomCalendarView customCalendarView;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_with_event);

        customCalendarView = (CustomCalendarView)findViewById(R.id.customCalendar);
        backBtn = (ImageButton)findViewById(R.id.backBtn);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }


}
