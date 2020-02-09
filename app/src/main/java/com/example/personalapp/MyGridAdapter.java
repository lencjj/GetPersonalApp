package com.example.personalapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Event> events;
    LayoutInflater inflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Event> events) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH);
        int displayYear = dateCalendar.get(Calendar.YEAR);
        //get the current month and year
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;

        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setMinimumHeight((parent.getHeight()/6) - (2 * (MainActivity.dpi/160)));

        }
        else{//days that are not actually part of the month
            view.setMinimumHeight((parent.getHeight()/6) - (2 * (MainActivity.dpi/160)));
            TextView textView = (TextView) view.findViewById(R.id.calendar_day);
            textView.setTextColor(getContext().getResources().getColor(R.color.opacitywhite));
        }


        TextView dayNumber = view.findViewById(R.id.calendar_day);
        dayNumber.setText(String.valueOf(dayNo));

        Calendar eventCalendar = Calendar.getInstance();
        int remainSpace = (parent.getHeight()/6) - (2 * (MainActivity.dpi/160)) - (30 * (MainActivity.dpi/160)); //remaining space left in the grid cell to display notes
        int numOfNotes = remainSpace/51; //number of notes the grid cell can container
        int numOfEvents = 0; // number of events on the specific day
        int count = 0;
        for(int i = 0; i < events.size(); i++) {
            eventCalendar.setTime(ConvertStringToDate(events.get(i).getDate())); //set eventCalendar time to the event date
            //Check if display day number matches event day number and if display month matches event month
            if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH)
                    && displayYear == eventCalendar.get(Calendar.YEAR)) {
                count++;
                Log.d("TESTING", Integer.toString(count));
                numOfEvents++;
                View container = view.findViewById(R.id.calendar_day_container);
                if (count == numOfNotes){ //When creating the last note the grid cell can contain
                    Log.d("TESTING", "Im in IF");
                    TextView lastNote = new TextView(view.getContext());
                    lastNote.setTag("lastNote");
                    lastNote.setText(events.get(i).getEvent());
                    lastNote.setTextColor(getContext().getResources().getColor(R.color.black));
                    lastNote.setBackground(getContext().getResources().getDrawable(R.drawable.event_note_background));
                    lastNote.setMaxLines(1);
                    lastNote.setPadding(2,1,2,1);
                    lastNote.setTextSize(10);
                    lastNote.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 47);
                    params.setMargins(0,0,0,3);
                    lastNote.setLayoutParams(params);
                    ((LinearLayout)container).addView(lastNote);
                }
                else if(count > numOfNotes){ //When the maximum number of notes have been reached
                    Log.d("TESTING", "Im in ELSE IF");
                    TextView lastNote = (TextView) container.findViewWithTag("lastNote");
                    lastNote.setText(numOfEvents - numOfNotes + 1 +" more...");
                }
                else{
                    Log.d("TESTING", "Im in ELSE");
                    TextView newNote = new TextView(view.getContext());
                    newNote.setText(events.get(i).getEvent());
                    newNote.setTextColor(getContext().getResources().getColor(R.color.black));
                    newNote.setBackground(getContext().getResources().getDrawable(R.drawable.event_note_background));
                    newNote.setMaxLines(1);
                    newNote.setPadding(2,1,2,1);
                    newNote.setTextSize(10);
                    newNote.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 47);
                    params.setMargins(0,0,0,3);
                    newNote.setLayoutParams(params);
                    ((LinearLayout)container).addView(newNote);
                }
            }
        }
        return view;

    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
        }catch(ParseException e){
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
