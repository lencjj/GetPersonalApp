package com.example.personalapp.ArchitectureComponents;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.personalapp.Entity.Expense;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses, todayExpenses,
            janExpenses, febExpenses, marExpenses, aprExpenses, mayExpenses, junExpenses,
            julExpenses, augExpenses, sepExpenses, octExpenses, novExpenses, decExpenses;
    private LiveData<Double> totalExpenses;
    private LiveData<Integer> yearsInRecord;



    public ExpenseViewModel(@NonNull Application application) { // so that we can pass the application in the constructor
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        totalExpenses = repository.getTotalExpenses();
        todayExpenses = repository.getTodayExpenses();
        yearsInRecord =  repository.getYearsInRecord();
        // months
        janExpenses = repository.getJanExpenses();
        febExpenses = repository.getFebExpenses();
        marExpenses = repository.getMarExpenses();
        aprExpenses = repository.getAprExpenses();
        mayExpenses = repository.getMayExpenses();
        junExpenses = repository.getJunExpenses();
        julExpenses = repository.getJulExpenses();
        augExpenses = repository.getAugExpenses();
        sepExpenses = repository.getSepExpenses();
        octExpenses = repository.getOctExpenses();
        novExpenses = repository.getNovExpenses();
        decExpenses = repository.getDecExpenses();
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }
    public void update(Expense expense) {
        repository.update(expense);
    }
    public void delete(Expense expense) {
        repository.delete(expense);
    }
    public void deleteAllExpenses() {
        repository.deleteAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }
    public LiveData<Double> getTotalExpenses() {
        return totalExpenses;
    }
    public LiveData<List<Expense>> getTodayExpenses() {
        return todayExpenses;
    }
    public LiveData<Integer> getYearsInRecord() {
        return yearsInRecord;
    }
    // months
    public LiveData<List<Expense>> getJanExpenses() {
        return janExpenses;
    }
    public LiveData<List<Expense>> getFebExpenses() {
        return febExpenses;
    }
    public LiveData<List<Expense>> getMarExpenses() {
        return marExpenses;
    }
    public LiveData<List<Expense>> getAprExpenses() {
        return aprExpenses;
    }
    public LiveData<List<Expense>> getMayExpenses() {
        return mayExpenses;
    }
    public LiveData<List<Expense>> getJunExpenses() {
        return junExpenses;
    }
    public LiveData<List<Expense>> getJulExpenses() {
        return julExpenses;
    }
    public LiveData<List<Expense>> getAugExpenses() {
        return augExpenses;
    }
    public LiveData<List<Expense>> getSepExpenses() {
        return sepExpenses;
    }
    public LiveData<List<Expense>> getOctExpenses() {
        return octExpenses;
    }
    public LiveData<List<Expense>> getNovExpenses() {
        return novExpenses;
    }
    public LiveData<List<Expense>> getDecExpenses() {
        return decExpenses;
    }



}
