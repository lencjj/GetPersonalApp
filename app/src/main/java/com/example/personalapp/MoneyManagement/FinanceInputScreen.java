package com.example.personalapp.MoneyManagement;
// JingHui's



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
import com.example.personalapp.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import static android.widget.Toast.LENGTH_SHORT;



public class FinanceInputScreen extends AppCompatActivity { // input form doesnt communicate with other layers
    // HAVE TO CHANGE WHEN MERGE WITH PEOPLE CODES BECAUSE PACKAGE WILL CHANGE (CODING IN FLOW)
    // Package name to keep it unique
    public static final String EXTRA_ID = "com.example.personalapp.MoneyManagement.EXTRA_ID";
    public static final String EXTRA_MEMO = "com.example.personalapp.MoneyManagement.EXTRA_MEMO";
    public static final String EXTRA_MONEY = "com.example.personalapp.MoneyManagement.EXTRA_MONEY";
    public static final String EXTRA_TYPE = "com.example.personalapp.MoneyManagement.EXTRA_TYPE";
    public static final String EXTRA_DATE = "com.example.personalapp.MoneyManagement.EXTRA_DATE";
    public static final String EXTRA_YEAR = "com.example.personalapp.MoneyManagement.EXTRA_YEAR";
    public static final String EXTRA_MONTH = "com.example.personalapp.MoneyManagement.EXTRA_MONTH";
    public static final String EXTRA_YEARMONTH = "com.example.personalapp.MoneyManagement.EXTRA_YEARMONTH";
    public static final String EXTRA_DAY = "com.example.personalapp.MoneyManagement.EXTRA_DAY";
    public static final String EXTRA_TIME = "com.example.personalapp.MoneyManagement.EXTRA_TIME";

    private EditText inputMoneyText, inputMemoText;
    private ImageButton backButton;
    private Button save_as_income_btn, save_as_expense_btn, edit_btn;
    private TextView title;
    private Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneymanagement_input_screen);
        // init -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        intent = getIntent();

        backButton = (ImageButton) findViewById(R.id.backButton);
        inputMemoText = (EditText) findViewById(R.id.inputMemo_txt);
        inputMoneyText = (EditText) findViewById(R.id.inputMoney_txt);
        save_as_income_btn = (Button) findViewById(R.id.save_as_income_btn);
        save_as_expense_btn = (Button) findViewById(R.id.save_as_expense_btn);
        edit_btn = (Button) findViewById(R.id.edit_btn);

        // Edit
        title = (TextView) findViewById(R.id.add_interface_title);

        // init -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------



        // Method set to 2 decimal
        inputMoneyText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        // OnClick
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinanceInputScreen.this, MainActivity_Finance.class);
                startActivity(intent);
//                finish();
//                onBackPressed(); // finish(); // it brings back to the previous activity
            }
        });

        save_as_income_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // get
                String strInputMemo = inputMemoText.getText().toString();
                String strInputMoney = inputMoneyText.getText().toString();
                // Save method
                saveNewFinance(strInputMemo, strInputMoney, "income");
            }
        });
        save_as_expense_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // get
                String strInputMemo = inputMemoText.getText().toString();
                String strInputMoney = inputMoneyText.getText().toString();
                // Save method
                saveNewFinance(strInputMemo, strInputMoney, "expense");
            }
        });



        // INCOME decision HERE
        if (intent.hasExtra(EXTRA_ID)) { // Edit needs id to check, true if it contains
            title.setText("Edit the record");
            save_as_expense_btn.setVisibility(View.GONE);
            save_as_income_btn.setVisibility(View.GONE);
            edit_btn.setVisibility(View.VISIBLE);
            inputMemoText.setText(intent.getStringExtra(EXTRA_MEMO));
            inputMoneyText.setText(intent.getDoubleExtra(EXTRA_MONEY, 0.00)+"0");
        } else {
            title.setText("Add a record");
        }
        edit_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // get
                String strInputMemo = inputMemoText.getText().toString();
                String strInputMoney = inputMoneyText.getText().toString();
                // Save method
                saveNewFinance(strInputMemo, strInputMoney, intent.getStringExtra(EXTRA_TYPE));
            }
        });


    } // End onCreate

    private void saveNewFinance(String strInputMemo, String strInputMoney, String strInputType) {
//        toastMassage(strInputMoney+", "+strInputMemo);
        if (strInputMemo.trim().isEmpty() || strInputMoney.trim().isEmpty()) {
            toastMassage("Please enter the field(s)!");
            return; // stop
        }
        if (strInputType.equals("expense")) {
            Double negative = Double.parseDouble(strInputMoney) - (2 * (Double.parseDouble(strInputMoney)));
            strInputMoney = negative.toString();
        }
        // To send data back the activity
        Intent newData = new Intent();
        newData.putExtra(EXTRA_MEMO, strInputMemo);
        newData.putExtra(EXTRA_MONEY, strInputMoney);
        newData.putExtra(EXTRA_TYPE, strInputType);
        newData.putExtra(EXTRA_DATE, intent.getStringExtra(EXTRA_DATE));
        newData.putExtra(EXTRA_YEAR, intent.getStringExtra(EXTRA_YEAR));
        newData.putExtra(EXTRA_MONTH, intent.getStringExtra(EXTRA_MONTH));
        newData.putExtra(EXTRA_YEARMONTH, intent.getStringExtra(EXTRA_YEARMONTH));
        newData.putExtra(EXTRA_DAY, intent.getStringExtra(EXTRA_DAY));
        newData.putExtra(EXTRA_TIME, intent.getStringExtra(EXTRA_TIME));

        // Edit:
        int id = getIntent().getIntExtra(EXTRA_ID, -1); // -1 means error if theres nth
        if (id != -1) {
            newData.putExtra(EXTRA_ID, id);
        }

        // send it back
        setResult(RESULT_OK, newData);
        finish();

    }


    private class DecimalDigitsInputFilter implements InputFilter {
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


    // for jinghui to test/demo dont delete!
    private void toastMassage(String msg) {
        Toast.makeText(this, msg, LENGTH_SHORT).show();
    }


}
