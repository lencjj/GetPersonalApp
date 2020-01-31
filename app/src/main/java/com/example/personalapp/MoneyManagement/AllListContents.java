package com.example.personalapp.MoneyManagement;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.personalapp.Adapters.ExpenseAdapter;
import com.example.personalapp.ArchitectureComponents.ExpenseViewModel;
import com.example.personalapp.Entity.Expense;
import com.example.personalapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllListContents extends AppCompatActivity {
    public static final int ADD_EXPENSE_REQUEST = 1;
    public static final int EDIT_EXPENSE_REQUEST = 2;
    private ExpenseViewModel expenseViewModel;
    private ImageButton backButton, deleteAllButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneymanagement_view_all);

        // init
        backButton = (ImageButton) findViewById(R.id.backButton);
        deleteAllButton = (ImageButton) findViewById(R.id.deleteAllButton);


        // OnClick
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // handle diff req


                Intent i = new Intent(AllListContents.this, MainActivity_MoneyManagement.class);
                startActivity(i);
            }
        });
        deleteAllButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // deleteAll
                // cfm msg
                expenseViewModel.deleteAllExpenses();
                toastMassage("You deleted all the record(s)!");
            }
        });



        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);



        expenseViewModel = ViewModelProviders
                .of(this) // the lifecycle will destroy the viewmodel when this activity is finished
                .get(ExpenseViewModel.class); // this is the vm i want to get the instance of

        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) { // will be triggered every time when the data changes
                // update RecyclerView later
                //

                adapter.submitList(expenses);

            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                // 3. expenseViewModel.delete()
                // 2. adapter.getExpenseAt()
                // 1. viewHolder.getAdapterPosition()


                Expense deletedExpense = adapter.getExpenseAt(position);

                // UNDO
//                adapter.notifyItemRemoved(position);
//                recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                expenseViewModel.delete(deletedExpense);

                // UNDO
//                Snackbar.make(recyclerView,
//                        "Deleting " + deletedExpense.getMemo() + " with $" + deletedExpense.getMoney() + ".",
//                        Snackbar.LENGTH_LONG)
//                        .setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toastMassage("clicked undo");
//                    }
//                }).show();

//                expenseViewModel.delete(adapter.getExpenseAt(viewHolder.getAdapterPosition())); // just 1 line
                toastMassage("Deleted!");
            }
        }).attachToRecyclerView(recyclerView);


        // BACK HERE (TO DECIDE ＩＮＣＯＭＥ or ..
        adapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(AllListContents.this, InputValueScreen.class);
                intent.putExtra(InputValueScreen.EXTRA_ID, expense.getId());
                intent.putExtra(InputValueScreen.EXTRA_MEMO, expense.getMemo());
                intent.putExtra(InputValueScreen.EXTRA_MONEY, expense.getMoney());
                startActivityForResult(intent, EDIT_EXPENSE_REQUEST);
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

            expenseViewModel.insert(expense);

            toastMassage("Saved!!!");

        } else if (requestCode == EDIT_EXPENSE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(InputValueScreen.EXTRA_ID, -1);

            if (id == -1) {
                // sth goes wrong
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
