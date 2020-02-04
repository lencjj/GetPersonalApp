package com.example.personalapp.Entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String memo;

    private Double money;

    private String date;

    private String year;

    private String month;

    private String day;

    private String time;

//    private String time;
//    @Ignore
//    private String dateOnly, timeOnly;  // will rename


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
    public String getYear() {
        return year;
    }
    public String getMonth() {
        return month;
    }
    public String getDay() {
        return day;
    }
    public String getTime() {
        return time;
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
    public void setYear(String year) {
        this.year = year;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public void setTime(String time) {
        this.time = time;
    }


}
