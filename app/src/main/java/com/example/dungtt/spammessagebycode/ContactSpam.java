package com.example.dungtt.spammessagebycode;

public class ContactSpam {
    private int _id;
    private String numberSpam;
    private int count;

    public ContactSpam(String numberSpam, int count) {
        this.numberSpam = numberSpam;
        this.count = count;
    }

    public ContactSpam(int _id, String numberSpam, int count) {
        this._id = _id;
        this.numberSpam = numberSpam;
        this.count = count;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getNumberSpam() {
        return numberSpam;
    }

    public void setNumberSpam(String numberSpam) {
        this.numberSpam = numberSpam;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
