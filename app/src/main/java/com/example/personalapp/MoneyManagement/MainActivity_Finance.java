package com.example.personalapp.MoneyManagement;
// JingHui's




import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.example.personalapp.Adapters.FinanceAdapter;
import com.example.personalapp.ArchitectureComponents.FinanceViewModel;
import com.example.personalapp.MainActivity;
import com.example.personalapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalapp.Entity.Finance;
import com.google.android.material.snackbar.Snackbar;



public class MainActivity_Finance extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    public static final int ADD_RECORD_REQUEST = 0;
    public static final int EDIT_EXPENSE_REQUEST = 1;
    public static final int EDIT_INCOME_REQUEST = 2;
    private TextView totalExpenseText, totalIncomeText, balanceText, dropdownarrow_numberpickersarea_Text, todayExpensesTitleText;
    private FloatingActionButton floatingActionButton;
    private FinanceViewModel financeViewModel;
    private ImageButton backButton, dropdownarrow_numberpickersarea_Button, upnarrow_numberpickersarea_Button, refresh_Button;
    private FinanceAdapter adapter;

    // For numberpickers
    private NumberPicker numberPickerYear, numberPickerMonth;
    private int defaultYear, defaultMonth, intInputYearmonth;
    private String inputYear, inputMonth, inputYearmonth;

    // yyyyMMddHHmmss
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
    SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss aa");
    Date date = new Date();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneymanagement_main);

        // init -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        totalExpenseText = (TextView) findViewById(R.id.totalExpenseText);
        totalIncomeText = (TextView) findViewById(R.id.totalIncomeText);
        balanceText = (TextView) findViewById(R.id.balanceText);
        todayExpensesTitleText = (TextView) findViewById(R.id.todayExpensesTitleText);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addButton);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        backButton = (ImageButton) findViewById(R.id.backButton);
        dropdownarrow_numberpickersarea_Text = (TextView) findViewById(R.id.dropdownarrow_numberpickersarea_text);
        dropdownarrow_numberpickersarea_Button = (ImageButton) findViewById(R.id.dropdownnarrow_numberpickersarea);
        upnarrow_numberpickersarea_Button = (ImageButton) findViewById(R.id.upnarrow_numberpickersarea);
        refresh_Button = (ImageButton) findViewById(R.id.refresh_button);
        // Year
        numberPickerYear = (NumberPicker) findViewById(R.id.numberpicker_year);
        numberPickerYear.setMinValue(2010); // modify
        numberPickerYear.setMaxValue(2030);
        defaultYear = Integer.parseInt(yearFormatter.format(date)); // so it shows the year now
        numberPickerYear.setValue(defaultYear); // Hence, the app will show the year in the app as default
        inputYear = String.valueOf(defaultYear);
        numberPickerYear.setOnValueChangedListener(this);
        // Month
        numberPickerMonth = (NumberPicker) findViewById(R.id.numberpicker_month);
        numberPickerMonth.setMinValue(1); // modify
        numberPickerMonth.setMaxValue(12);
        defaultMonth = Integer.parseInt(monthFormatter.format(date));
        numberPickerMonth.setValue(defaultMonth); // Hence, the app will show the month in the app as default
        inputMonth = String.valueOf(defaultMonth);
        numberPickerMonth.setOnValueChangedListener(this);
        // for monthly income
        intInputYearmonth = Integer.parseInt(yearFormatter.format(date) + "" + monthFormatter.format(date));

        // Notification
        createReminder();
        // init -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        // OnClick
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Finance.this, MainActivity.class);
                startActivity(i);
            }
        });
        // filter
        dropdownarrow_numberpickersarea_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout numberpickersArea = (LinearLayout) findViewById(R.id.numberpickersArea);
                numberpickersArea.setVisibility(View.VISIBLE);
                dropdownarrow_numberpickersarea_Button.setVisibility(View.GONE);
                upnarrow_numberpickersarea_Button.setVisibility(View.VISIBLE);
                intInputYearmonth = Integer.parseInt(yearFormatter.format(date)+""+monthFormatter.format(date));
                getRecordsByYearmonth();
            }
        });
        dropdownarrow_numberpickersarea_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout numberpickersArea = (LinearLayout) findViewById(R.id.numberpickersArea);
                numberpickersArea.setVisibility(View.VISIBLE);
                dropdownarrow_numberpickersarea_Button.setVisibility(View.GONE);
                upnarrow_numberpickersarea_Button.setVisibility(View.VISIBLE);
                intInputYearmonth = Integer.parseInt(yearFormatter.format(date)+""+monthFormatter.format(date));
                getRecordsByYearmonth();
            }
        });

        upnarrow_numberpickersarea_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout numberpickersArea = (LinearLayout) findViewById(R.id.numberpickersArea);
                numberpickersArea.setVisibility(View.GONE);
                dropdownarrow_numberpickersarea_Button.setVisibility(View.VISIBLE);
                upnarrow_numberpickersarea_Button.setVisibility(View.GONE);
                getDefaultExpenses();
                todayExpensesTitleText.setText("today expense");
            }
        });
        refresh_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMassage("Clicked");
                // Livedata
//                financeViewModel = ViewModelProviders
//                        .of(MainActivity_Finance.this) // the lifecycle will destroy the viewmodel when this activity is finished
//                        .get(FinanceViewModel.class); // this is the vm i want to get the instance of

                Intent reload = new Intent(MainActivity_Finance.this, MainActivity_Finance.class);
                startActivity(reload);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_Finance.this, FinanceInputScreen.class);
                startActivityForResult(i, ADD_RECORD_REQUEST);

            }
        });



        // RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // size never changes
        adapter = new FinanceAdapter();
        recyclerView.setAdapter(adapter);

        // Livedata
        financeViewModel = ViewModelProviders
                .of(this) // the lifecycle will destroy the viewmodel when this activity is finished
                .get(FinanceViewModel.class); // this is the vm i want to get the instance of

        // i decide to show today data as the first data user can see/view
        // Default record(s)
        getDefaultExpenses();

        // filter date
        // i got this method so i can check how many years are there
        financeViewModel.getYearsInRecord().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                if (!strings.isEmpty()) {
                    numberPickerYear.setMinValue(Integer.parseInt(strings.get(0)));
                    if (Integer.parseInt(strings.get(0)) == 1) {
                        numberPickerYear.setMaxValue(Integer.parseInt(strings.get(0)));
                    } else {
                        numberPickerYear.setMaxValue(Integer.parseInt(strings.get(0)) + strings.size() - 1);
                    }
                }
            }

        });


        // Delete --------------------------------------------------------------------------------------------------------------------------------------------------------------------
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Finance deletedFinance = adapter.getFinanceAt(position);
                String deletedFinanceMoney = "";
                if (deletedFinance.getMoney().toString().contains("-")) {
                    String parts[] = deletedFinance.getMoney().toString().split("\\-");
                    deletedFinanceMoney = parts[1];
                } else {
                    deletedFinanceMoney = deletedFinance.getMoney().toString();
                }
                Snackbar.make(recyclerView,
                        "Do you want to delete \""  + deletedFinance.getMemo() + "\" with $ " + deletedFinanceMoney + ".",
                        Snackbar.LENGTH_LONG)
                        .setAction("DELETE!", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toastMassage("Delete!");
                                financeViewModel.delete(deletedFinance);
                            }
                        }).show();

                adapter.notifyDataSetChanged();

//                financeViewModel.delete(adapter.getFinanceAt(viewHolder.getAdapterPosition())); // just 1 line

            }
        }).attachToRecyclerView(recyclerView);


        // Edit --------------------------------------------------------------------------------------------------------------------------------------------------------------------
        adapter.setOnItemClickListener(new FinanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Finance finance) {
                Intent intent = new Intent(MainActivity_Finance.this, FinanceInputScreen.class);

                intent.putExtra(FinanceInputScreen.EXTRA_ID, finance.getId());
                intent.putExtra(FinanceInputScreen.EXTRA_MEMO, finance.getMemo());
                intent.putExtra(FinanceInputScreen.EXTRA_DATE, finance.getDate());
                intent.putExtra(FinanceInputScreen.EXTRA_YEAR, finance.getYear());
                intent.putExtra(FinanceInputScreen.EXTRA_MONTH, finance.getMonth());
                intent.putExtra(FinanceInputScreen.EXTRA_YEARMONTH, finance.getYearmonth());
                intent.putExtra(FinanceInputScreen.EXTRA_DAY, finance.getDay());
                intent.putExtra(FinanceInputScreen.EXTRA_TIME, finance.getTime());

                if (finance.getType().equals("expense")) {
                    // Edit expense
                    String parts[] = finance.getMoney().toString().split("\\-");
                    intent.putExtra(FinanceInputScreen.EXTRA_MONEY, Double.parseDouble(parts[1]));
                    intent.putExtra(FinanceInputScreen.EXTRA_TYPE, finance.getType());
                    startActivityForResult(intent, EDIT_EXPENSE_REQUEST);
                } else {
                    // Edit income
                    intent.putExtra(FinanceInputScreen.EXTRA_MONEY, finance.getMoney());
                    intent.putExtra(FinanceInputScreen.EXTRA_TYPE, finance.getType());
                    startActivityForResult(intent, EDIT_INCOME_REQUEST);
                }

            }
        });



    } // End onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // when return back from inputscreen
        super.onActivityResult(requestCode, resultCode, data);
        Finance finance;
        String memo = data.getStringExtra(FinanceInputScreen.EXTRA_MEMO);
        Double money = Double.parseDouble(data.getStringExtra(FinanceInputScreen.EXTRA_MONEY));
        String type = data.getStringExtra(FinanceInputScreen.EXTRA_TYPE);


        if (requestCode == ADD_RECORD_REQUEST && resultCode == RESULT_OK) { // i can find which req i handle

            finance = new Finance(memo, money);
            finance.setDate(dateFormatter.format(date));
            finance.setYear(yearFormatter.format(date));
            finance.setMonth(monthFormatter.format(date));
            finance.setYearmonth(finance.getYear()+""+finance.getMonth());
            finance.setDay(dayFormatter.format(date));
            finance.setTime(timeFormatter.format(date));
            finance.setType(type);

            financeViewModel.insert(finance);

            toastMassage("Saved!");

        } else if ((requestCode == EDIT_EXPENSE_REQUEST || requestCode == EDIT_INCOME_REQUEST ) && resultCode == RESULT_OK) {
            int id = data.getIntExtra(FinanceInputScreen.EXTRA_ID, -1);
            String edit_date = data.getStringExtra(FinanceInputScreen.EXTRA_DATE);
            String edit_year = data.getStringExtra(FinanceInputScreen.EXTRA_YEAR);
            String edit_month = data.getStringExtra(FinanceInputScreen.EXTRA_MONTH);
            String edit_yearmonth = data.getStringExtra(FinanceInputScreen.EXTRA_YEARMONTH);
            String edit_day = data.getStringExtra(FinanceInputScreen.EXTRA_DAY);
            String edit_time = data.getStringExtra(FinanceInputScreen.EXTRA_TIME);


            if (id == -1) {
                // sth goes wrong
                toastMassage("Cant be updated!");
                return;
            }

            finance = new Finance(memo, money);
            finance.setId(id);
            finance.setDate(edit_date);
            finance.setYear(edit_year);
            finance.setMonth(edit_month);
            finance.setYearmonth(edit_yearmonth);
            finance.setDay(edit_day);
            finance.setTime(edit_time);
            finance.setType(type);

            financeViewModel.update(finance);

            toastMassage("Updated!");

        } else {
            toastMassage("Not saved..");
        }

    }


    // filter ------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if (numberPicker == numberPickerYear) {
            inputYear = String.valueOf(i1);
        } else if (numberPicker == numberPickerMonth) {
            if (i1 < 10) {
                inputMonth = "0" + i1;
            } else {
                inputMonth = String.valueOf(i1);
            }
        } else {
            // nth
        }

        inputYearmonth = inputYear + "" + inputMonth;
        // convert
        intInputYearmonth = Integer.parseInt(inputYearmonth);

//        toastMassage("You've selected " + intInputYearmonth);

        getRecordsByYearmonth();

    }

    public void getRecordsByYearmonth() {
        financeViewModel.getFinanceRecordsByYearmonth(intInputYearmonth).observe(this, new Observer<List<Finance>>() {
            @Override
            public void onChanged(@Nullable List<Finance> finances) {
                adapter.submitList(finances);
            }
        });

        financeViewModel.getTotalIncomesByYearmonth(intInputYearmonth).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    totalIncomeText.setText("$ 0.00");
                } else {
                    totalIncomeText.setText("$ " + aDouble + "0");
                }
            }
        });
        financeViewModel.getTotalExpensesByYearmonth(intInputYearmonth).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                todayExpensesTitleText.setText("monthly expense");
                if (aDouble == null) {
                    totalExpenseText.setText("$ 0.00");
                } else {
                    if (aDouble.toString().contains("-")) {
                        String parts[] = aDouble.toString().split("\\-");
                        totalExpenseText.setText("-$ " + parts[1] + "0");
                    } else {
                        totalExpenseText.setText("$ " + aDouble + "0");
                    }
                }
            }
        });
        financeViewModel.getBalanceByYearmonth(intInputYearmonth).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    balanceText.setText("$ 0.00");
                } else {
                    if (aDouble.toString().contains("-")) {
                        String parts[] = aDouble.toString().split("\\-");
                        balanceText.setText("-$ " + parts[1] + "0");
                    } else {
                        balanceText.setText("$ " + aDouble + "0");
                    }
                }
            }
        });
    }


    public void getDefaultExpenses() {
        financeViewModel.getTodayFinanceRecords().observe(this, new Observer<List<Finance>>() {
            @Override
            public void onChanged(@Nullable List<Finance> finances) { // will be triggered every time when the data changes
                adapter.submitList(finances);
            }
        });
        financeViewModel.getTotalIncomesByYearmonth(intInputYearmonth).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    totalIncomeText.setText("$ 0.00");
                } else {
                    totalIncomeText.setText("$ " + aDouble + "0");
                }
            }
        });
        financeViewModel.getTotalExpensesByToday().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    totalExpenseText.setText("$ 0.00");
                    // Notification ------------------------------------------------------------------------------------------------------------------------------------

                    toastMassage("No record.");

                    Intent intent = new Intent(MainActivity_Finance.this, FinanceReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity_Finance.this, 0, intent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    long timeNow = System.currentTimeMillis();
                    long tenSecondsInMills = 1000 * 10;

                    alarmManager.set(AlarmManager.RTC_WAKEUP, (timeNow + tenSecondsInMills), pendingIntent);

                } else {
                    if(aDouble.toString().contains("-")) {
                        String parts[] = aDouble.toString().split("\\-");
                        totalExpenseText.setText("-$ " + parts[1] + "0");
                    } else {
                        totalExpenseText.setText("$ " + aDouble + "0");
                    }
                }
            }
        });
        financeViewModel.getBalanceByYearmonth(intInputYearmonth).observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                if (aDouble == null) {
                    balanceText.setText("$ 0.00");
                } else {
                    if (aDouble.toString().contains("-")) {
                        String parts[] = aDouble.toString().split("\\-");
                        balanceText.setText("-$ " + parts[1] + "0");
                    } else {
                        balanceText.setText("$ " + aDouble + "0");
                    }
                }
            }
        });

    }


    private void createReminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String desc = "desc";
            int imp = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel("financeReminder", name, imp);
            notificationChannel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }


    // for jinghui to test/demo dont delete!
    private void toastMassage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
