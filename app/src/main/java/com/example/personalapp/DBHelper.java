package com.example.personalapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private final static String CREATE_EVENTS_TABLE = "create table " +DBStructure.EVENT_TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            +DBStructure.NAME+ " TEXT, "
            +DBStructure.TIME+ " TEXT, "
            +DBStructure.MINUTES+ " INTEGER, "
            +DBStructure.DATE+ " TEXT, "
            +DBStructure.MONTH+ " TEXT, "
            +DBStructure.YEAR+ " TEXT)";
    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " +DBStructure.EVENT_TABLE_NAME;

    public DBHelper(@Nullable Context context){
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_EVENTS_TABLE);
        onCreate(db);
    }

    public void SaveEvent(String name, String time, int minutes, String date, String month, String year, SQLiteDatabase database)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.NAME, name);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.MINUTES, minutes);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        database.insert(DBStructure.EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor ReadEvents(String date, SQLiteDatabase database){
        String[] Projections = {DBStructure.NAME, DBStructure.TIME, DBStructure.MINUTES, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String Selection = DBStructure.DATE + "=?";
        String[] SelectionArguments = {date};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArguments, null, null, DBStructure.MINUTES +" ASC");
    }


    public Cursor ReadEventsMonth(String month, String year, SQLiteDatabase database){
        String[] Projections = {DBStructure.NAME, DBStructure.TIME, DBStructure.MINUTES, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String Selection = DBStructure.MONTH + "=? and " +DBStructure.YEAR + "=?";
        String[] SelectionArguments = {month,year};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArguments, null, null, DBStructure.MINUTES +" ASC");
    }

    public void deleteEvent(String name, String date, String time, SQLiteDatabase database){
        String selection = DBStructure.NAME+ "=? and " +DBStructure.DATE+ "=? and "+DBStructure.TIME+ " =?";
        String[] selectionArguments = {name, date, time};
        database.delete(DBStructure.EVENT_TABLE_NAME, selection, selectionArguments);
    }

    public void updateEvent(String name, String date, String time, String newName, String newTime, int newMinutes, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.NAME, newName);
        contentValues.put(DBStructure.TIME, newTime);
        contentValues.put(DBStructure.MINUTES, newMinutes);
        String selection = DBStructure.NAME+ "=? and " +DBStructure.DATE+ "=? and "+DBStructure.TIME+ " =?";
        String[] selectionArguments = {name, date, time};
        database.update(DBStructure.EVENT_TABLE_NAME, contentValues, selection, selectionArguments);
    }

}
