package com.track.model;

import java.time.LocalDate;

public class track {
        private LocalDate date;
        private   double income;
        private double expense;
        private String description;
        private double balance;

    public track(LocalDate date, double income, double expense, String description, double balance) {
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.description = description;
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
   


}
