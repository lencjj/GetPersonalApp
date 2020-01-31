package com.example.personalapp.ArchitectureComponents;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.personalapp.Entity.Expense;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;



    public ExpenseViewModel(@NonNull Application application) { // so that we can pass the application in the constructor
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
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
}
