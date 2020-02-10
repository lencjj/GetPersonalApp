package com.example.personalapp.ArchitectureComponents;



import android.app.Application;
import android.os.AsyncTask;
import java.util.List;
import androidx.lifecycle.LiveData;
import com.example.personalapp.Entity.Finance;



public class FinanceRepository {

    private FinanceDao financeDao;
    private LiveData<List<Finance>> allFinanceRecords, todayFinanceRecords;
    private LiveData<Double> totalExpenses, totalIncomes, totalExpensesByToday, totalIncomesByToday, balanceByToday, balanceByYearmonth;
    private LiveData<List<String>> yearsInRecord;
    private LiveData<Finance> lastRecord;



    public FinanceRepository(Application application) {     // application is a subclass of context,
        // use it as a context to create database instance

        FinanceDatabase database = FinanceDatabase.getInstance(application);
        financeDao = database.financeDao(); // abstract method that created in FinanceDatabase.java
        // normally we cant call it
        // but i created instance w the databaseBuilder,
        // Room auto-generate all the codes for this method !!!

        allFinanceRecords = financeDao.getAllFinanceRecords();
        todayFinanceRecords = financeDao.getTodayFinanceRecords();

        totalExpenses = financeDao.getTotalExpenses();
        totalIncomes = financeDao.getTotalIncomes();
        totalExpensesByToday = financeDao.getTotalExpensesByToday();
        totalIncomesByToday = financeDao.getTotalIncomesByToday();
        balanceByToday = financeDao.getBalanceByToday();

        yearsInRecord  = financeDao.getYearsInRecord();
        lastRecord = financeDao.getLastRecord();



    } // End of ER



    // methods here are the APIs for the outside to call
    public void insert(Finance finance) {
        new InsertFinanceAsyncTask(financeDao).execute(finance);
    }
    public void update(Finance finance) {
        new UpdateFinanceAsyncTask(financeDao).execute(finance);
    }
    public void delete(Finance finance) {
        new DeleteFinanceAsyncTask(financeDao).execute(finance);
    }
    public void deleteAllFinances() {
        new DeleteAllFinancesAsyncTask(financeDao).execute();
    }
    // Room will auto execute the database on the background, but the method above: insert, update, delete, deleteAllFinances
    public LiveData<List<Finance>> getAllFinanceRecords() {
        return allFinanceRecords;
    }
    public LiveData<List<Finance>> getTodayFinanceRecords() {
        return todayFinanceRecords;
    }
    public LiveData<List<Finance>> getFinanceRecordsByYearmonth(int inputYearmonth) {
        GetFinanceRecordsByYearMonthAsyncTask getFinanceRecordsByYearMonthAsyncTask = new GetFinanceRecordsByYearMonthAsyncTask(financeDao);
        LiveData<List<Finance>> liveData = getFinanceRecordsByYearMonthAsyncTask.GetLiveData(inputYearmonth);
        return liveData;
    }


    public LiveData<Double> getTotalExpenses() {
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
        GetTotalExpensesByYearmonthAsyncTask getTotalExpensesByYearmonth = new GetTotalExpensesByYearmonthAsyncTask(financeDao);
        LiveData<Double> liveData = getTotalExpensesByYearmonth.GetLiveData(inputYearmonth);
        return liveData;
    }
    public LiveData<Double> getTotalIncomesByYearmonth(int inputYearmonth) {
        GetTotalIncomesByYearmonthAsyncTask getTotalIncomesByYearmonthAsyncTask = new GetTotalIncomesByYearmonthAsyncTask(financeDao);
        LiveData<Double> liveData = getTotalIncomesByYearmonthAsyncTask.GetLiveData(inputYearmonth);
        return liveData;
    }
    public LiveData<Double> getBalanceByYearmonth(int inputYearmonth) {
        GetBalanceByYearmonthAsyncTask getBalanceByYearmonthAsyncTask = new GetBalanceByYearmonthAsyncTask(financeDao);
        LiveData<Double> liveData = getBalanceByYearmonthAsyncTask.GetLiveData(inputYearmonth);
        return liveData;
    }


    public LiveData<List<String>> getYearsInRecord() {
        return yearsInRecord;
    }
    public LiveData<Finance> getLastRecord() { return lastRecord; }





    // My method -----------------------------------------------------------------------------------------------------------
    private static class GetFinanceRecordsByYearMonthAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Finance>>> {
        private FinanceDao financeDao;

        private GetFinanceRecordsByYearMonthAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }
        @Override
        protected LiveData<List<Finance>> doInBackground(Integer... integers) { // Integers is like an array
            return financeDao.getFinanceRecordsByYearmonth(integers[0]);
        }

        public LiveData<List<Finance>> GetLiveData(int inputYearmonth) {
            return doInBackground(inputYearmonth);
        }

    }
    private static class GetTotalExpensesByYearmonthAsyncTask extends AsyncTask<Integer, Void, LiveData<Double>> {
        private FinanceDao financeDao;

        private GetTotalExpensesByYearmonthAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }
        @Override
        protected LiveData<Double> doInBackground(Integer... integers) { // Integers is like an array
            return financeDao.getTotalExpensesByYearmonth(integers[0]);
        }
        public LiveData<Double> GetLiveData(int inputYearmonth) {
            return doInBackground(inputYearmonth);
        }

    }
    private static class GetTotalIncomesByYearmonthAsyncTask extends AsyncTask<Integer, Void, LiveData<Double>> {
        private FinanceDao financeDao;

        private GetTotalIncomesByYearmonthAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }
        @Override
        protected LiveData<Double> doInBackground(Integer... integers) { // Integers is like an array
            return financeDao.getTotalIncomesByYearmonth(integers[0]);
        }
        public LiveData<Double> GetLiveData(int inputYearmonth) {
            return doInBackground(inputYearmonth);
        }

    }
    private static class GetBalanceByYearmonthAsyncTask extends AsyncTask<Integer, Void, LiveData<Double>> {
        private FinanceDao financeDao;

        private GetBalanceByYearmonthAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }
        @Override
        protected LiveData<Double> doInBackground(Integer... integers) { // Integers is like an array
            return financeDao.getBalanceByYearmonth(integers[0]);
        }
        public LiveData<Double> GetLiveData(int inputYearmonth) {
            return doInBackground(inputYearmonth);
        }

    }


    // static is best to prevent memories leak
    private static class InsertFinanceAsyncTask extends AsyncTask<Finance, Void, Void> {
        private FinanceDao financeDao;

        private InsertFinanceAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }

        @Override
        protected Void doInBackground(Finance... finances) {
            financeDao.insert(finances[0]);
            return null;
        }
    }
    private static class UpdateFinanceAsyncTask extends AsyncTask<Finance, Void, Void> {
        private FinanceDao financeDao;

        private UpdateFinanceAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }

        @Override
        protected Void doInBackground(Finance... finances) {
            financeDao.update(finances[0]);
            return null;
        }
    }
    private static class DeleteFinanceAsyncTask extends AsyncTask<Finance, Void, Void> {
        private FinanceDao financeDao;

        private DeleteFinanceAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }

        @Override
        protected Void doInBackground(Finance... finances) {
            financeDao.delete(finances[0]);
            return null;
        }
    }
    private static class DeleteAllFinancesAsyncTask extends AsyncTask<Void, Void, Void> {
        private FinanceDao financeDao;

        private DeleteAllFinancesAsyncTask(FinanceDao financeDao) {
            this.financeDao = financeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            financeDao.deleteAllFinances();
            return null;
        }
    }

}
