package com.example.personalapp.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String memo;

    private Double money;

    private String date;

    @Ignore
    private Double total;

    public Expense(String memo, double money) {
        this.memo = memo;
        this.money = money;
    }

//    public Expense(int id, String memo, double money) {
//        this.id = id;
//        this.memo = memo;
//        this.money = money;
//    }

    public int getId() {
        return id;
    }

    public String getMemo() {
        return memo;
    }

    public Double getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
