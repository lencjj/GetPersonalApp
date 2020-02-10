package com.example.personalapp.JournalEntry;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalEntryRepo {
    private JournalEntryData journalEntryData;
    private LiveData<List<JournalEntry>> allJournals;

    public JournalEntryRepo(Application application){
        JournalEntryDB database = JournalEntryDB.getInstance(application);
        journalEntryData = database.journalEntryData();
        allJournals = journalEntryData.getJournals();
    }

    public void insert(JournalEntry journalEntry){
        new InsertJournalAsyncTask(journalEntryData).execute(journalEntry);
    }

    public void update(JournalEntry journalEntry){
        new UpdateJournalAsyncTask(journalEntryData).execute(journalEntry);
    }
    public void delete(JournalEntry journalEntry){
        new DeleteJournalAsyncTask(journalEntryData).execute(journalEntry);
    }
    public void deleteJournals(){
        new DeleteJournalsAsyncTask(journalEntryData).execute();
    }

    public LiveData<List<JournalEntry>> getAllJournals(){
        return allJournals;
    }

    private static class InsertJournalAsyncTask extends AsyncTask<JournalEntry, Void, Void>{
        private JournalEntryData journalEntryData;
        private InsertJournalAsyncTask(JournalEntryData journalEntryData){
            this.journalEntryData = journalEntryData;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalEntryData.insert(journalEntries[0]);
            return null;
        }
    }

    private static class UpdateJournalAsyncTask extends AsyncTask<JournalEntry, Void, Void>{
        private JournalEntryData journalEntryData;
        private UpdateJournalAsyncTask(JournalEntryData journalEntryData){
            this.journalEntryData = journalEntryData;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalEntryData.update(journalEntries[0]);
            return null;
        }
    }

    private static class DeleteJournalAsyncTask extends AsyncTask<JournalEntry, Void, Void>{
        private JournalEntryData journalEntryData;
        private DeleteJournalAsyncTask(JournalEntryData journalEntryData){
            this.journalEntryData = journalEntryData;
        }

        @Override
        protected Void doInBackground(JournalEntry... journalEntries) {
            journalEntryData.delete(journalEntries[0]);
            return null;
        }
    }

    private static class DeleteJournalsAsyncTask extends AsyncTask<Void, Void, Void>{
        private JournalEntryData journalEntryData;
        private DeleteJournalsAsyncTask(JournalEntryData journalEntryData){
            this.journalEntryData = journalEntryData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            journalEntryData.deleteJournals();
            return null;
        }
    }

}

