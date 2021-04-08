package com.example.project;

public class Reminder {
    private int mID;
    private String mTitle;
    private String mContact;
    private String mDate;
    private String mTime;
    private String mAddress;
    private String mActive;


    public Reminder(int ID, String Title, String Contact, String Date, String Time, String Address,String Active){
        mID = ID;
        mTitle = Title;
        mContact = Contact;
        mDate = Date;
        mTime = Time;
        mAddress = Address;
        mActive = Active;
    }

    public Reminder(String Title, String Contact, String Date, String Time, String Address, String Active){
        mTitle = Title;
        mContact = Contact;
        mDate = Date;
        mTime = Time;
        mAddress = Address;
        mActive = Active;
    }

    public Reminder(){}

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContact() {return mContact;}

    public  void setContact(String Contact) { mContact = Contact; }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }
}

