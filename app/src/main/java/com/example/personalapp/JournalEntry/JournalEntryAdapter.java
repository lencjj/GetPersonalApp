package com.example.personalapp.JournalEntry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalapp.R;

public class JournalEntryAdapter extends ListAdapter<JournalEntry, JournalEntryAdapter.JournalEntryHolder> {

    private OnItemClickListener listener;

    protected JournalEntryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<JournalEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<JournalEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull JournalEntry oldItem, @NonNull JournalEntry newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull JournalEntry oldItem, @NonNull JournalEntry newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDesc().equals(newItem.getDesc());
        }
    };



    @NonNull
    @Override
    public JournalEntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_journal_entry, parent, false);
        return new JournalEntryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalEntryHolder holder, int position) {
        JournalEntry currJournal = getItem(position);
        holder.txtViewTitle.setText(currJournal.getTitle());
        holder.txtViewDesc.setText(currJournal.getDesc());
    }

    public JournalEntry getJournalAt(int position){
        return getItem(position);
    }

    class JournalEntryHolder extends RecyclerView.ViewHolder{
        private TextView txtViewTitle;
        private TextView txtViewDesc;

        public JournalEntryHolder(@NonNull View itemView) {
            super(itemView);
            txtViewTitle = itemView.findViewById(R.id.txtTitle);
            txtViewDesc = itemView.findViewById(R.id.txtDesc);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(JournalEntry journalEntry);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

