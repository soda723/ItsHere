package com.capstone.itshere.accountBook;

public class DailyNote {
    private String date;
    private String category;
    private String note;
    private int amount;


    public DailyNote(){}

    public DailyNote(String date, String category, String note, int amount) {
        this.date = date;
        this.category = category;
        this.note = note;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return Integer.toString(amount);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
