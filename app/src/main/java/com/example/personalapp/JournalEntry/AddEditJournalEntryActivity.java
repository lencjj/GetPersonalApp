package com.example.personalapp.JournalEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalapp.R;

public class AddEditJournalEntryActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.personalApp.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.personalApp.EXTRA_TITLE";
    public static final String EXTRA_DESC =
            "com.example.personalApp.EXTRA_DESC";

    private EditText editTxtTitle;
    private EditText editTxtDesc;
    private ImageButton btn_Back;
    private ImageButton btn_Save;

    private void saveJournal(){
        String title = editTxtTitle.getText().toString();
        String desc = editTxtDesc.getText().toString();

        if(title.trim().isEmpty() || desc.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, desc);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal_entry);

        editTxtTitle = findViewById(R.id.edit_txtTitle);
        editTxtDesc = findViewById(R.id.edit_txtDesc);
        btn_Back = (ImageButton) findViewById(R.id.btnBack);
        btn_Save = (ImageButton) findViewById(R.id.btnSave);

        btn_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddEditJournalEntryActivity.this, MainActivity_JournalEntry.class);
                startActivity(i);
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveJournal();
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Journal Entry");
            editTxtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTxtDesc.setText(intent.getStringExtra(EXTRA_DESC));
        }else {
            setTitle("Add Journal Entry");
        }
    }
}
