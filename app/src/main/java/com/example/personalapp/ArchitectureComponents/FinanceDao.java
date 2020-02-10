package com.example.personalapp.ArchitectureComponents;



import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.personalapp.Entity.Finance;



@Dao // Data Access Object (DAO)
public interface FinanceDao { // only provide methods

    // room will automatically generate the all the codes, as long as u annotate it
    @Insert
    void insert(Finance finance);

    @Update
    void update(Finance finance);

    @Delete
    void delete(Finance finance);

    @Query("DELETE FROM finance_table") // its better compare to u write it in SQLite cuz it shows error (DEMO)
    void deleteAllFinances();



    // Records
    @Query("SELECT * FROM finance_table ORDER BY date DESC, id DESC") // order by date
    LiveData<List<Finance>> getAllFinanceRecords();  // LiveData can obverse the object,
    // if there are any changes in the table it will automatically be updated

    // my gay gay method start here -----------------------------------------------------------------------------
    @Query("SELECT * FROM finance_table WHERE DATE(date) = DATE() ORDER BY date DESC, id DESC")
    LiveData<List<Finance>> getTodayFinanceRecords();

    @Query("SELECT * FROM finance_table WHERE yearmonth = :inputYearmonth ORDER BY date DESC, id DESC")
    LiveData<List<Finance>> getFinanceRecordsByYearmonth(int inputYearmonth);




    // Double
    @Query("SELECT SUM(money) AS expense FROM finance_table WHERE type = 'expense'")
    LiveData<Double> getTotalExpenses();
    @Query("SELECT SUM(money) AS income FROM finance_table WHERE type = 'income'")
    LiveData<Double> getTotalIncomes();

    @Query("SELECT SUM(money) FROM finance_table WHERE DATE(date) = DATE() AND type = 'expense'")
    LiveData<Double> getTotalExpensesByToday();
    @Query("SELECT SUM(money) FROM finance_table WHERE DATE(date) = DATE() AND type = 'income'")
    LiveData<Double> getTotalIncomesByToday();
    @Query("SELECT SUM(money) FROM finance_table WHERE DATE(date) = DATE()")
    LiveData<Double> getBalanceByToday();

    @Query("SELECT SUM(money) FROM finance_table WHERE yearmonth = :inputYearmonth AND type = 'expense'")
    LiveData<Double> getTotalExpensesByYearmonth(int inputYearmonth);
    @Query("SELECT SUM(money) FROM finance_table WHERE yearmonth = :inputYearmonth AND type = 'income'")
    LiveData<Double> getTotalIncomesByYearmonth(int inputYearmonth);
    @Query("SELECT SUM(money) FROM finance_table WHERE yearmonth = :inputYearmonth")
    LiveData<Double> getBalanceByYearmonth(int inputYearmonth);




    @Query("SELECT DISTINCT year FROM finance_table")
    LiveData<List<String>> getYearsInRecord();

    @Query("SELECT * FROM finance_table WHERE id=(SELECT max(id) FROM finance_table)")
    LiveData<Finance> getLastRecord();















}
