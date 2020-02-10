package com.example.personalapp.JournalEntry;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = JournalEntry.class, version = 1, exportSchema=false)
public abstract class JournalEntryDB extends RoomDatabase {

    private static JournalEntryDB instance;
    public abstract JournalEntryData journalEntryData();

    public static synchronized JournalEntryDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    JournalEntryDB.class, "journalentryDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private JournalEntryData journalEntryData;
        private PopulateDbAsyncTask(JournalEntryDB db){
            journalEntryData = db.journalEntryData();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            journalEntryData.insert(new JournalEntry("Hello Kristine", "How are you?"));
            journalEntryData.insert(new JournalEntry("Hello Lennel", "How are you?"));
            journalEntryData.insert(new JournalEntry("Hello Jing Hui", "How are you?"));
            return null;
        }
    }
}
