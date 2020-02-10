package com.example.personalapp.JournalEntry;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalEntryViewModel extends AndroidViewModel {
    private JournalEntryRepo repo;
    private LiveData<List<JournalEntry>> allJournals;

    public JournalEntryViewModel(@NonNull Application application) {
        super(application);
        repo = new JournalEntryRepo(application);
        allJournals = repo.getAllJournals();
    }

    public void insert(JournalEntry journalEntry){
        repo.insert(journalEntry);
    }

    public void update(JournalEntry journalEntry){
        repo.update(journalEntry);
    }

    public void delete(JournalEntry journalEntry){
        repo.delete(journalEntry);
    }

    public void deleteJournals(){
        repo.deleteJournals();
    }

    public LiveData<List<JournalEntry>> getAllJournals(){
        return allJournals;
    }
}
