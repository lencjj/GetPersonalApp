package com.example.personalapp.Entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "finance_table")
public class Finance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String memo;

    private Double money;

    private String date;

    private String yearmonth;

    private String year;

    private String month;

    private String day;

    private String time;

    private String type;




    public Finance(String memo, Double money) {
        this.memo = memo;
        this.money = money;
    }



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
    public String getYearmonth() {
        return yearmonth;
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
    public String getType() {
        return type;
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
    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
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
    public void setType(String type) {
        this.type = type;
    }


}
