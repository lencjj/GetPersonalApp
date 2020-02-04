package com.example.personalapp.ArchitectureComponents;


import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import com.example.personalapp.Entity.Expense;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses, todayExpenses,
            janExpenses, febExpenses, marExpenses, aprExpenses, mayExpenses, junExpenses,
            julExpenses, augExpenses, sepExpenses, octExpenses, novExpenses, decExpenses;
    private LiveData<Double> totalExpenses;
    private LiveData<Integer> yearsInRecord;


    public ExpenseRepository(Application application) {     // application is a subclass of context,
        // use it as a context to create database instance

        ExpenseDatabase database = ExpenseDatabase.getInstance(application);
        expenseDao = database.expenseDao(); // abstract method that created in ExpenseDatabase.java
        // normally we cant call it
        // but i created instance w the databaseBuilder,
        // Room auto-generate all the codes for this method !!!

        allExpenses = expenseDao.getAllExpenses();
        totalExpenses = expenseDao.getTotalExpenses();
        todayExpenses = expenseDao.getTodayExpenses();
        yearsInRecord  = expenseDao.getYearsInRecord();

        // months
        janExpenses = expenseDao.getJanExpenses();
        febExpenses = expenseDao.getFebExpenses();
        marExpenses = expenseDao.getMarExpenses();
        aprExpenses = expenseDao.getAprExpenses();
        mayExpenses = expenseDao.getMayExpenses();
        junExpenses = expenseDao.getJunExpenses();
        julExpenses = expenseDao.getJulExpenses();
        augExpenses = expenseDao.getAugExpenses();
        sepExpenses = expenseDao.getSepExpenses();
        octExpenses = expenseDao.getOctExpenses();
        novExpenses = expenseDao.getNovExpenses();
        decExpenses = expenseDao.getDecExpenses();


    } // End of ER

    // methods here are the APIs for the outside to call
    public void insert(Expense expense) {
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }
    public void update(Expense expense) {
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }
    public void delete(Expense expense) {
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }
    public void deleteAllExpenses() {
        new DeleteAllExpensesAsyncTask(expenseDao).execute();
    }
    // Room will auto execute the database on the background, but the method above: insert, update, delete, deleteAllExpenses

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







    private static class InsertExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private InsertExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.insert(expenses[0]);
            return null;
        }
    }
    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private UpdateExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.update(expenses[0]);
            return null;
        }
    }
    private static class DeleteExpenseAsyncTask extends AsyncTask<Expense, Void, Void> {
        private ExpenseDao expenseDao;

        private DeleteExpenseAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            expenseDao.delete(expenses[0]);
            return null;
        }
    }
    private static class DeleteAllExpensesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExpenseDao expenseDao;

        private DeleteAllExpensesAsyncTask(ExpenseDao expenseDao) {
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            expenseDao.deleteAllExpenses();
            return null;
        }
    }

}
