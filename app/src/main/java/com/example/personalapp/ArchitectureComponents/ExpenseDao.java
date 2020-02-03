package com.example.personalapp.ArchitectureComponents;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.personalapp.Entity.Expense;

@Dao // Data Access Object (DAO)
public interface ExpenseDao { // only provide methods

    // room will automatically generate the all the codes, as long as u annotate it
    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expense_table") // its better compare to u write it in SQLite cuz it shows error (DEMO)
    void deleteAllExpenses();

    @Query("SELECT * FROM expense_table ORDER BY id DESC")
    LiveData<List<Expense>> getAllExpenses();  // LiveData can obverse the object,
    // if there are any changes in the table it will automatically be updated

    // my gay gay method
    @Query("SELECT SUM(money) FROM expense_table")
    LiveData<Double> getTotalExpenses();



}
