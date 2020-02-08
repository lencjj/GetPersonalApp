package com.example.personalapp.JournalEntry;

import android.text.format.DateFormat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Locale;

@Entity(tableName = "tblJournal")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String desc;
    private String date;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate(long time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JournalEntry(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
}
