package com.youkeda.wacai.web.model;

public class Accounting {
    private int cash;
    private int income;
    private int charges;
    private int rent;
    private int eat;
    private int treat;
    private int ktv;
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) { this.cash = cash; }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getEat() {
        return eat;
    }

    public void setEat(int eat) {
        this.eat = eat;
    }

    public int getTreat() {
        return treat;
    }

    public void setTreat(int treat) {
        this.treat = treat;
    }

    public int getKtv() {
        return ktv;
    }

    public void setKtv(int ktv) {
        this.ktv = ktv;
    }
}
