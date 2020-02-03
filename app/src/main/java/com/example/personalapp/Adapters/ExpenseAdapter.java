package com.example.personalapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalapp.Entity.Expense;
import com.example.personalapp.R;


public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ExpenseHolder> { // recyclerview knows this is the viewholder we wanna use
    //    private List<Expense> expenses = new ArrayList<>(); // to stack the data before to get the new data update
    // Now save it to the ListAdapter
    private OnItemClickListener listener;

    public ExpenseAdapter() {
        super(DIFF_CALLBACK);
    }


    // static bc have to pass it to super class | final bc it fixed
    private static final DiffUtil.ItemCallback<Expense> DIFF_CALLBACK = new DiffUtil.ItemCallback<Expense>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getId() == newItem.getId(); // the same item, same id
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return (
                    oldItem.getMemo().equals(newItem.getMemo())
                            &&
                            // compare double
                            Double.parseDouble(oldItem.getMoney().toString()) == Double.parseDouble(newItem.getMoney().toString())
            );
        }
    };


    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) // parent is the recyclerview itself
                .inflate(R.layout.cardviewcontents, parent, false);
        return new ExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        Expense currentExpense = getItem(position);
        holder.textViewMemo.setText(currentExpense.getMemo());
        holder.textViewMoney.setText(String.valueOf(currentExpense.getMoney()));
    }

    public Expense getExpenseAt(int position) { // LIDDAT ALSO CAN UH WTF SICK
        return getItem(position);
    }


    class ExpenseHolder extends RecyclerView.ViewHolder {
        private TextView textViewMemo;
        private TextView textViewMoney;

        public ExpenseHolder(View itemView) {
            super(itemView); // card itself
            textViewMemo = itemView.findViewById(R.id.cardtextview_memo);
            textViewMoney = itemView.findViewById(R.id.cardtextview_money);

            // call the customised methods
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });

        }

    }

    // get click event, create interface
    public interface OnItemClickListener { // only declare methods
        void onItemClick(Expense expense);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



}

