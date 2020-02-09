package com.example.personalapp.MoneyManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalapp.Adapters.ExpenseAdapter;
import com.example.personalapp.ArchitectureComponents.ExpenseViewModel;
import com.example.personalapp.MainActivity;
import com.example.personalapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalapp.Entity.Expense;

public class MainActivity_MoneyManagement extends AppCompatActivity {
    public static final int ADD_INCOME_REQUEST = 1;
    public static final int ADD_EXPENSE_REQUEST = 2;
    public static final int EDIT_EXPENSE_REQUEST = 3;
    private TextView viewAllText, totalExpenseText, totalIncomeText, balanceText;
    private FloatingActionButton floatingActionButton;
    private ExpenseViewModel expenseViewModel;
    private ImageButton backButton;

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

        // init
        viewAllText = (TextView) findViewById(R.id.viewAllExpenses);
        totalExpenseText = (TextView) findViewById(R.id.totalExpenseText);
//        totalIncomeText = (TextView) findViewById(R.id.totalIncomeText);
//        balanceText = (TextView) findViewById(R.id.balanceText);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addButton);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        backButton = (ImageButton) findViewById(R.id.backButton);


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_MoneyManagement.this, MainActivity.class);
                startActivity(i);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_MoneyManagement.this, InputValueScreen.class);
                startActivityForResult(i, ADD_EXPENSE_REQUEST);

            }
        });
        viewAllText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity_MoneyManagement.this, AllListContents.class);
                startActivity(i);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // size never changes


        // Disable scrolling
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);



        // Livedata
        expenseViewModel = ViewModelProviders
                .of(this) // the lifecycle will destroy the viewmodel when this activity is finished
                .get(ExpenseViewModel.class); // this is the vm i want to get the instance of
        expenseViewModel.getTodayExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) { // will be triggered every time when the data changes
                adapter.submitList(expenses);
                // TO FIX TOP
//                recyclerView.smoothScrollToPosition(0);
            }

        });
        expenseViewModel.getTotalExpenses().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
//                toastMassage("Checking: total is $" + aDouble);
                totalExpenseText.setText("$ " + aDouble + "0");

            }
        });

        // set income
        // filter date
//        expenseViewModel.getYearsInRecord().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer aInteger) {
//                toastMassage("Checking: year is " + aInteger);
//            }
//        });




    } // End onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // convert
//        TimestampConverter timestampConverter = new TimestampConverter();

        if (requestCode == ADD_EXPENSE_REQUEST && resultCode == RESULT_OK) { // i can find which req i handle
            String memo = data.getStringExtra(InputValueScreen.EXTRA_MEMO);
            Double money = Double.parseDouble(data.getStringExtra(InputValueScreen.EXTRA_MONEY));

            Expense expense = new Expense(memo, money);
            expense.setDate(dateFormatter.format(date));
            expense.setYear(yearFormatter.format(date));
            expense.setMonth(monthFormatter.format(date));
            expense.setDay(dayFormatter.format(date));
            expense.setTime(timeFormatter.format(date));

            expenseViewModel.insert(expense);
            toastMassage("Saved!!!");

        } else {
            toastMassage("Not saved..");
        }
    }




    private void toastMassage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}
