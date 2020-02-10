package com.example.personalapp.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalapp.Entity.Finance;
import com.example.personalapp.R;


public class FinanceAdapter extends ListAdapter<Finance, FinanceAdapter.FinanceHolder> { // recyclerview knows this is the viewholder we wanna use
    //    private List<Finance> finances = new ArrayList<>(); // to stack the data before to get the new data update
    // Now save it to the ListAdapter
    private OnItemClickListener listener;

    public FinanceAdapter() {
        super(DIFF_CALLBACK);
    }

    // static bc have to pass it to super class | final bc it fixed
    private static final DiffUtil.ItemCallback<Finance> DIFF_CALLBACK = new DiffUtil.ItemCallback<Finance>() {
        @Override
        public boolean areItemsTheSame(@NonNull Finance oldItem, @NonNull Finance newItem) {
            return oldItem.getId() == newItem.getId(); // the same item, same id
        }
        @Override
        public boolean areContentsTheSame(@NonNull Finance oldItem, @NonNull Finance newItem) {
            return (
                oldItem.getMemo().equals(newItem.getMemo())
                &&
                Double.parseDouble(oldItem.getMoney().toString()) == Double.parseDouble(newItem.getMoney().toString())
            );
        }
    };


    @NonNull
    @Override
    public FinanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) // parent is the recyclerview itself
                .inflate(R.layout.cardviewcontents, parent, false);
        return new FinanceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceHolder holder, int position) {
        Finance currentFinance = getItem(position);
        holder.textViewMemo.setText(currentFinance.getMemo());
        holder.textViewDate.setText(currentFinance.getDate());
        holder.textViewTime.setText(currentFinance.getTime());
        if (currentFinance.getType().equals("expense")) {
            String parts[] = currentFinance.getMoney().toString().split("\\-");
            holder.textViewMoney.setText("-$ " + parts[1] + "0");
            holder.textViewMoney.setTextColor(Color.parseColor("#A74A41"));
        } else if (currentFinance.getType().equals("income")){
            holder.textViewMoney.setText("$ "+currentFinance.getMoney()+"0");
            holder.textViewMoney.setTextColor(Color.parseColor("#42B350"));
        }
    }

    public Finance getFinanceAt(int position) { // LIDDAT ALSO CAN UH WTF SICK
        return getItem(position);
    }


    class FinanceHolder extends RecyclerView.ViewHolder {
        private TextView textViewMemo;
        private TextView textViewDate;
        private TextView textViewTime;
        private TextView textViewMoney;

        public FinanceHolder(View itemView) {
            super(itemView); // card itself
            textViewMemo = itemView.findViewById(R.id.cardtextview_memo);
            textViewDate = itemView.findViewById(R.id.cardtextview_date);
            textViewTime = itemView.findViewById(R.id.cardtextview_time);
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
        void onItemClick(Finance finance);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



}

