package com.example.personalapp.JournalEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalapp.MainActivity;
import com.example.personalapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity_JournalEntry extends AppCompatActivity {
    public static final int ADD_JOURNAL_REQ = 1;
    public static final int EDIT_JOURNAL_REQ = 2;
    private JournalEntryViewModel journalEntryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalentry_main);

        ImageButton btn_back = findViewById(R.id.btnBack);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_JournalEntry.this, MainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton btnAddJournal = findViewById(R.id.btnFloatingAdd);
        btnAddJournal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_JournalEntry.this, AddEditJournalEntryActivity.class);
                startActivityForResult(intent, ADD_JOURNAL_REQ);
            }
        });

        RecyclerView recycler_View = findViewById(R.id.recyclerView);
        recycler_View.setLayoutManager(new LinearLayoutManager(this));
        recycler_View.setHasFixedSize(true);

        final JournalEntryAdapter adapter = new JournalEntryAdapter();
        recycler_View.setAdapter(adapter);

        journalEntryViewModel = ViewModelProviders.of(this).get(JournalEntryViewModel.class);
        journalEntryViewModel.getAllJournals().observe(this, new Observer<List<JournalEntry>>(){
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries){
                //update RecyclerView
                adapter.submitList(journalEntries);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                journalEntryViewModel.delete(adapter.getJournalAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity_JournalEntry.this,"Journal deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recycler_View);

        adapter.setOnItemClickListener(new JournalEntryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(JournalEntry journalEntry) {
                Intent intent = new Intent(MainActivity_JournalEntry.this, AddEditJournalEntryActivity.class);
                intent.putExtra(AddEditJournalEntryActivity.EXTRA_ID, journalEntry.getId());
                intent.putExtra(AddEditJournalEntryActivity.EXTRA_TITLE, journalEntry.getTitle());
                intent.putExtra(AddEditJournalEntryActivity.EXTRA_DESC, journalEntry.getDesc());
                startActivityForResult(intent, EDIT_JOURNAL_REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);

        if (reqCode == ADD_JOURNAL_REQ && resCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditJournalEntryActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditJournalEntryActivity.EXTRA_DESC);

            JournalEntry journalEntry = new JournalEntry(title, desc);
            journalEntryViewModel.insert(journalEntry);

            Toast.makeText(this, "Journal saved!", Toast.LENGTH_SHORT).show();
        } else if(reqCode == EDIT_JOURNAL_REQ && resCode == RESULT_OK){
            int id = data.getIntExtra(AddEditJournalEntryActivity.EXTRA_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Journal entry cannot be updated!", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditJournalEntryActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditJournalEntryActivity.EXTRA_DESC);


            JournalEntry journalEntry = new JournalEntry(title, desc);
            journalEntry.setId(id);
            journalEntryViewModel.update(journalEntry);
            Toast.makeText(this, "Journal entry is updated!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Journal not saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
