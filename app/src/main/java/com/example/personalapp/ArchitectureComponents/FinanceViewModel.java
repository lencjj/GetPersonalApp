package com.example.personalapp.ArchitectureComponents;



import android.app.Application;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.personalapp.Entity.Finance;



public class FinanceViewModel extends AndroidViewModel {
    private FinanceRepository repository;
    private LiveData<List<Finance>> allFinanceRecords, todayFinanceRecords;
    private LiveData<Double> totalExpenses, totalIncomes, totalExpensesByToday, totalIncomesByToday, balanceByToday, balanceByYearmonth;
    private LiveData<List<String>> yearsInRecord;
    private Double balance;
    private LiveData<Finance> lastRecord;



    public FinanceViewModel(@NonNull Application application) { // so that we can pass the application in the constructor
        super(application);
        repository = new FinanceRepository(application);

        // Livedata
        allFinanceRecords = repository.getAllFinanceRecords();
        todayFinanceRecords = repository.getTodayFinanceRecords();

        // Double
        totalExpenses = repository.getTotalExpenses();
        totalIncomes = repository.getTotalIncomes();
        totalExpensesByToday = repository.getTotalExpensesByToday();
        totalIncomesByToday = repository.getTotalIncomesByToday();
        balanceByToday = repository.getBalanceByToday();

        // String
        yearsInRecord =  repository.getYearsInRecord();

        // Finance
        lastRecord = repository.getLastRecord();

    }



    public void insert(Finance finance) {
        repository.insert(finance);
    }
    public void update(Finance finance) {
        repository.update(finance);
    }
    public void delete(Finance finance) {
        repository.delete(finance);
    }
    public void deleteAllFinances() {
        repository.deleteAllFinances();
    }



    public LiveData<List<Finance>> getAllFinanceRecords() {
        return allFinanceRecords;
    }
    public LiveData<List<Finance>> getTodayFinanceRecords() {
        return todayFinanceRecords;
    }
    public LiveData<List<Finance>> getFinanceRecordsByYearmonth(int inputYearmonth) {
        return repository.getFinanceRecordsByYearmonth(inputYearmonth);
    }


//    public double getBalance() {
//        return balance;
//    }
    public LiveData<Double> getTotalFinances() {
        return totalExpenses;
    }
    public LiveData<Double> getTotalIncomes() {
        return totalIncomes;
    }
    public LiveData<Double> getTotalExpensesByToday() {
        return totalExpensesByToday;
    }
    public LiveData<Double> getTotalIncomesByToday() {
        return totalIncomesByToday;
    }
    public LiveData<Double> getBalanceByToday() {
        return balanceByToday;
    }
    public LiveData<Double> getTotalExpensesByYearmonth(int inputYearmonth) {
        return repository.getTotalExpensesByYearmonth(inputYearmonth);
    }
    public LiveData<Double> getTotalIncomesByYearmonth(int inputYearmonth) {
        return repository.getTotalIncomesByYearmonth(inputYearmonth);
    }
    public LiveData<Double> getBalanceByYearmonth(int inputYearmonth) {
        return repository.getBalanceByYearmonth(inputYearmonth);
    }


    public LiveData<List<String>> getYearsInRecord() {
        return yearsInRecord;
    }

    public LiveData<Finance> getLastRecord() {
        return lastRecord;
    }


}
