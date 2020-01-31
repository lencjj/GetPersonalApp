package com.example.personalapp.MoneyManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalapp.Adapters.ExpenseAdapter;
import com.example.personalapp.ArchitectureComponents.ExpenseViewModel;
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
    private TextView viewAll;
    private FloatingActionButton floatingActionButton;
    private ExpenseViewModel expenseViewModel;
    private ImageButton backButton;

    // yyyyMMddHHmmss
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date = new Date();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneymanagement_main);

        // init
        viewAll = (TextView) findViewById(R.id.viewAllExpenses);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addButton);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        backButton = (ImageButton) findViewById(R.id.backButton);


        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
//                onBackPressed(); // finish(); // it brings back to the previous activity
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // choose INCOME or EXPENSE
                Intent i = new Intent(MainActivity_MoneyManagement.this, InputValueScreen.class);
//                startActivity(i);
                startActivityForResult(i, ADD_EXPENSE_REQUEST);

            }
        });

        viewAll.setOnClickListener(new View.OnClickListener(){
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




        expenseViewModel = ViewModelProviders
                .of(this) // the lifecycle will destroy the viewmodel when this activity is finished
                .get(ExpenseViewModel.class); // this is the vm i want to get the instance of
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) { // will be triggered every time when the data changes
                adapter.submitList(expenses);
                // TO FIX TOP
//                recyclerView.smoothScrollToPosition(0);

            }

        });



    } // End onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXPENSE_REQUEST && resultCode == RESULT_OK) { // i can find which req i handle
            String memo = data.getStringExtra(InputValueScreen.EXTRA_MEMO);
            Double money = Double.parseDouble(data.getStringExtra(InputValueScreen.EXTRA_MONEY));

            Expense expense = new Expense(memo, money);
            expense.setDate(formatter.format(date));
//            toastMassage(expense.getDate());
            expenseViewModel.insert(expense);
            toastMassage("Saved!!!");

        } else if (requestCode == EDIT_EXPENSE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(InputValueScreen.EXTRA_ID, -1);

            if (id == -1) { // sth goes wrong
                toastMassage("Cant be updated!");
                return;
            }

            String memo = data.getStringExtra(InputValueScreen.EXTRA_MEMO);
            Double money = Double.parseDouble(data.getStringExtra(InputValueScreen.EXTRA_MONEY));

            Expense expense = new Expense(memo, money);
            expense.setId(id);

            expenseViewModel.update(expense);

            toastMassage("Updated!");

        } else {
            toastMassage("Not saved..");
        }
    }



    private void toastMassage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
