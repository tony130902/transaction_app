package com.example.android.transactioncalculator;

public class transactionData {

    private int amount;
    private String message;
    private boolean positive;

    public transactionData(int amount , String message , boolean positive){
        this.amount = amount;
        this.message = message;
        this.positive = positive;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
}
