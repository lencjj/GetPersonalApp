package com.example.personalapp.JournalEntry;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JournalEntryData {

    @Insert
    void insert(JournalEntry journalentry);

    @Update
    void update(JournalEntry journalentry);

    @Delete
    void delete(JournalEntry journalentry);

    @Query("DELETE FROM tblJournal")
    void deleteJournals();

    @Query("SELECT * FROM tblJournal ORDER BY title DESC")
    LiveData<List<JournalEntry>> getJournals();
}

