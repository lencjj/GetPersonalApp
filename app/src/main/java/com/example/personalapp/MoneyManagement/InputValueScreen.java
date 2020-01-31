package com.example.personalapp.MoneyManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalapp.ArchitectureComponents.ExpenseViewModel;
import com.example.personalapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;

public class InputValueScreen extends AppCompatActivity { // input form doesnt communicate with other layers
    // HAVE TO CHANGE WHEN MERGE WITH PEOPLE CODES BECAUSE PACKAGE WILL CHANGE (CODING IN FLOW)
    // Package name to keep it unique
    public static final String EXTRA_ID = "com.example.personalapp.MoneyManagement.EXTRA_ID";
    public static final String EXTRA_MEMO = "com.example.personalapp.MoneyManagement.EXTRA_MEMO";
    public static final String EXTRA_MONEY = "com.example.personalapp.MoneyManagement.EXTRA_MONEY";
    private ExpenseViewModel expenseViewModel;
    private EditText inputMoneyText, inputMemoText;
    private ImageButton backButton, saveButton;
    private TextView titleTextView;
    private Button save_as_income_btn, save_as_expense_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneymanagement_input_screen);

        // init
        backButton = (ImageButton) findViewById(R.id.backButton);
//        saveButton = (ImageButton) findViewById(R.id.saveButton); // will be removed
        inputMemoText = (EditText) findViewById(R.id.inputMemo_txt);
        inputMoneyText = (EditText) findViewById(R.id.inputMoney_txt);
        save_as_expense_btn = (Button) findViewById(R.id.save_as_expense_btn);

        // Method set to 2 decimal
        inputMoneyText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        // OnClick
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
//                onBackPressed(); // finish(); // it brings back to the previous activity
            }
        });
        save_as_expense_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // get
                String strInputMemo = inputMemoText.getText().toString();
                String strInputMoney = inputMoneyText.getText().toString();

                // Save method
                saveNewExpense(strInputMemo, strInputMoney);
            }
        });

        // Edit

        TextView title = (TextView) findViewById(R.id.add_interface_title);

        Intent intent = getIntent();

        // INCOME decision HERE
        if (intent.hasExtra(EXTRA_ID)) { // true if it contains
            title.setText("Edit ");
//            toastMassage(intent.getDoubleExtra(EXTRA_MONEY, 0.0)+"!");
            inputMemoText.setText(intent.getStringExtra(EXTRA_MEMO));
            inputMoneyText.setText(intent.getDoubleExtra(EXTRA_MONEY, 0.00)+"");

        } else {
            title.setText("Add ");
        }


    } // End onCreate

    private void saveNewExpense(String strInputMemo, String strInputMoney) {
//        toastMassage(strInputMoney+", "+strInputMemo);
        if (strInputMemo.trim().isEmpty() || strInputMoney.trim().isEmpty()) {
            toastMassage("Please enter the field(s)!");
            return; // stop
        }
        // To send data back the activity
        Intent newData = new Intent();
        newData.putExtra(EXTRA_MEMO, strInputMemo);
        newData.putExtra(EXTRA_MONEY, strInputMoney);

        // Edit:
        int id = getIntent().getIntExtra(EXTRA_ID, -1); // -1 means error if theres nth
        if (id != -1) {
            newData.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, newData);
        finish();

    }


    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;

        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    private void toastMassage(String msg) {
        Toast.makeText(this, msg, LENGTH_SHORT).show();
    }


}
