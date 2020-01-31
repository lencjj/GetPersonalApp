package com.example.personalapp.ArchitectureComponents;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.personalapp.Entity.Expense;

@Database(entities = {Expense.class}, version = 1, exportSchema = false) // added  exportSchema = false
public abstract class ExpenseDatabase extends RoomDatabase {

    private static ExpenseDatabase instance; // instance means 1 (single) cant create multiple instance

    public abstract ExpenseDao expenseDao();

    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (instance == null) { // we dh then we create
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpenseDatabase.class, "expense_database")
                    .fallbackToDestructiveMigration() // can increase the version number
                    //
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExpenseDao expenseDao;

        private PopulateDbAsyncTask(ExpenseDatabase db) {
            expenseDao = db.expenseDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            expenseDao.insert(new Expense("Memo 1 ", 2.30));
            expenseDao.insert(new Expense("Memo 2 ", 3.50));
            expenseDao.insert(new Expense("Memo 3 ", 4.80));
            return null;
        }
    }

}
