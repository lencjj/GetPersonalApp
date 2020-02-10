package com.example.personalapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> arrayList;
    DBHelper dbHelper;

    public EventRecyclerAdapter(Context context, ArrayList<Event> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_row_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Event event = arrayList.get(position);
        holder.event.setText(event.getEvent());
        holder.dateText.setText(event.getDate());
        holder.time.setText(event.getTime());

        //delete event on click activity
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        deleteCalendarEvent(event.getEvent(), event.getDate(), event.getTime());
                        arrayList.remove(position);
                        notifyDataSetChanged();//Yes button clicked
                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //edit event on click activity
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.add_newevent_layout, null);

                final EditText eventName = popupView.findViewById(R.id.eventName);
                final TextView eventTime = popupView.findViewById(R.id.eventTime);
                ImageButton setTimeBtn = popupView.findViewById(R.id.setEventTimeBtn);
                final Button addEventBtn = popupView.findViewById(R.id.addEventBtn);

                eventName.setText(event.getEvent());
                eventName.setSelection(eventName.getText().length());
                eventTime.setText(event.getTime());
                addEventBtn.setText("SAVE");
                addEventBtn.setEnabled(true);
                addEventBtn.setBackgroundColor(Color.WHITE);
                addEventBtn.setTextColor(Color.BLACK);

                //Disable and enable add button
                eventName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String eventNameInput = eventName.getText().toString().trim();
                        addEventBtn.setEnabled(!eventNameInput.isEmpty());
                        if (addEventBtn.isEnabled()){
                            addEventBtn.setBackgroundColor(Color.WHITE);
                            addEventBtn.setTextColor(Color.BLACK);
                        }
                        else if (!addEventBtn.isEnabled()){
                            addEventBtn.setBackgroundColor(popupView.getContext().getResources().getColor(R.color.opacitywhite));
                            addEventBtn.setTextColor(Color.rgb(170, 170, 170));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                setTimeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(popupView.getContext(), R.style.TimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hFormat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String eventTimeStr = hFormat.format(c.getTime());
                                eventTime.setText(eventTimeStr);
                            }
                        },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });

                builder.setView(popupView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                addEventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateCalendarEvent(event.getEvent(), event.getDate(), event.getTime(), eventName.getText().toString(), eventTime.getText().toString(), convertStrToMins(eventTime.getText().toString()));
                        Intent i = new Intent(v.getContext(), CalendarWithEvent.class);
                        v.getContext().startActivity(i);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dateText, event, time;
        ImageButton deleteBtn, editBtn;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            dateText = itemView.findViewById(R.id.eventDate);
            event = itemView.findViewById(R.id.eventName);
            time = itemView.findViewById(R.id.eventTime);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }

    private void deleteCalendarEvent(String name, String date, String time){
        dbHelper = new DBHelper(context);
        try{
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            dbHelper.deleteEvent(name, date, time, database);
            Toast.makeText(context, "Delete successful", Toast.LENGTH_SHORT);
        } catch(Exception ex){
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT);
        }
        dbHelper.close();
    }

    private void updateCalendarEvent (String name, String date, String time, String newName, String newTime, int newMinutes){
        dbHelper = new DBHelper(context);
        try{
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            dbHelper.updateEvent(name, date, time, newName, newTime, newMinutes, database);
            Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT);
        } catch(Exception ex){
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT);

        }
        dbHelper.close();
    }

    public int convertStrToMins(String time){
        String[] strSplit = time.split(":");
        String[] strSplit2 = strSplit[1].split(" ");
        int hour = Integer.parseInt(strSplit[0]);
        int minutes = Integer.parseInt(strSplit2[0]);
        String period = strSplit2[1];
        int total = 0;

        if(period.equals("PM")){
            total = (12 * 60) + (hour * 60) + minutes;

        } else if(period.equals("AM")){
            total = (hour * 60) + minutes;
        }
        return total;
    }

}
