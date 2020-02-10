package com.example.personalapp.ArchitectureComponents;



import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.personalapp.Entity.Finance;




@Database(entities = {Finance.class}, version = 2, exportSchema = false) // added  exportSchema = false
public abstract class FinanceDatabase extends RoomDatabase {

    private static FinanceDatabase instance; // instance means 1 (single) cant create multiple instance

    public abstract FinanceDao financeDao();

    public static synchronized FinanceDatabase getInstance(Context context) {
        if (instance == null) { // we dh then we create
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FinanceDatabase.class, "finance_database")
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
        private FinanceDao financeDao;

        private PopulateDbAsyncTask(FinanceDatabase db) {
            financeDao = db.financeDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
