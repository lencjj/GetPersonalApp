package com.example.personalapp;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    ImageButton nextBtn, prevBtn, backBtn;
    TextView currentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Event> eventList = new ArrayList<>();
    DBHelper dbHelper;

    public CustomCalendarView(Context context){
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        //To navigate from one month to another
        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });


        //On click to add an event
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText eventName = addView.findViewById(R.id.eventName);
                final TextView eventTime = addView.findViewById(R.id.eventTime);
                ImageButton setTimeBtn = addView.findViewById(R.id.setEventTimeBtn);
                final Button addEventBtn = addView.findViewById(R.id.addEventBtn);

                setTimeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.TimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
                                String eventTimeStr = hFormat.format(c.getTime());
                                eventTime.setText(eventTimeStr);
                            }
                        },hours,minutes,false);
                        timePickerDialog.show();
                    }
                });

                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();

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
                            addEventBtn.setBackgroundColor(getContext().getResources().getColor(R.color.opacitywhite));
                            addEventBtn.setTextColor(Color.rgb(170, 170, 170));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                addEventBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       SaveEvent(eventName.getText().toString(), eventTime.getText().toString(), date, month, year);
                       SetUpCalendar();
                       alertDialog.dismiss();
                    }
                });
            }


        });

        //On long click to view the events for that day
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);

                RecyclerView recyclerView = showedView.findViewById(R.id.eventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showedView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showedView.getContext(), CollectEventByDate(date));
                recyclerView.setAdapter((eventRecyclerAdapter));
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showedView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });
                return true;
            }
        });
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);

        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn = view.findViewById(R.id.previousBtn);
        backBtn = view.findViewById(R.id.backBtn);
        currentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridView);

    }

    private void SaveEvent(String name, String time, String date, String month, String year){
        dbHelper = new DBHelper(context);
        try{
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            dbHelper.SaveEvent(name, time, convertStrToMins(time), date, month, year, database);
            Toast.makeText(context, "Event successfully saved", Toast.LENGTH_SHORT).show();
        } catch (Exception ex){
            Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();
    }

    private void SetUpCalendar(){
        int firstDayOfTheMonth;
        String currDate = dateFormat.format(calendar.getTime());
        currentDate.setText(currDate);
        dates.clear();

        Calendar monthCalendar = (Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1); //Set to first day of the month
        if (monthCalendar.get((Calendar.DAY_OF_WEEK)) == 1){
            firstDayOfTheMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) + 5; //in the case that sunday is first day of the month, Sunday = 1
        }
        else{
            firstDayOfTheMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 2; //-2 because Mon = 2
        }
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth); //To make Calendar date start from Monday
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while(dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventList);
        gridView.setAdapter(myGridAdapter);

    }

    private void CollectEventsPerMonth(String month, String year){
        eventList.clear();
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.ReadEventsMonth(month, year, database);
        while(cursor.moveToNext()){
            String eventName = cursor.getString(cursor.getColumnIndex(DBStructure.NAME));
            String eventTime = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            int eventMinutes = cursor.getInt(cursor.getColumnIndex(DBStructure.MINUTES));
            String eventDate = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String eventMonth = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String eventYear = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Event event = new Event(eventName, eventTime, eventMinutes, eventDate, eventMonth, eventYear);
            eventList.add(event);
        }
        cursor.close();
        dbHelper.close();
    }

    private ArrayList<Event> CollectEventByDate(String date){
        ArrayList<Event> arrayList = new ArrayList<>();
        dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.ReadEvents(date, database);
        while (cursor.moveToNext()){
            String eventName = cursor.getString(cursor.getColumnIndex(DBStructure.NAME));
            String eventTime = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            int eventMinutes = cursor.getInt(cursor.getColumnIndex(DBStructure.MINUTES));
            String eventDate = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String eventMonth = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String eventYear = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Event event = new Event(eventName, eventTime, eventMinutes, eventDate, eventMonth, eventYear);
            arrayList.add(event);
        }
        cursor.close();
        dbHelper.close();
        return arrayList;
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
