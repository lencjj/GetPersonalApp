package com.example.personalapp.ArchitectureComponents;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Query("SELECT * FROM expense_table ORDER BY date DESC, id DESC") // order by date
    LiveData<List<Expense>> getAllExpenses();  // LiveData can obverse the object,
    // if there are any changes in the table it will automatically be updated

    // my gay gay method start here -----------------------------------------------------------------------------
    @Query("SELECT SUM(money) FROM expense_table")
    LiveData<Double> getTotalExpenses();

    @Query("SELECT * FROM expense_table WHERE DATE(date) = DATE() ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getTodayExpenses();

    @Query("SELECT COUNT(DISTINCT year) FROM expense_table") // works (DEMO)
    LiveData<Integer> getYearsInRecord();

    // get diff month
    @Query("SELECT * FROM expense_table WHERE month = '01' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getJanExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '02' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getFebExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '03' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getMarExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '04' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getAprExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '05' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getMayExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '06' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getJunExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '07' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getJulExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '08' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getAugExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '09' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getSepExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '10' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getOctExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '11' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getNovExpenses();
    @Query("SELECT * FROM expense_table WHERE month = '12' ORDER BY date DESC, id DESC")
    LiveData<List<Expense>> getDecExpenses();



}
