package com.example.project;

public class Reminder {
    private int mID;
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mRepeat;
    private String mRepeatTime;
    private String mAddress;
    private String mActive;


    public Reminder(int ID, String Title, String Date, String Time, String Repeat, String RepeatTime, String Address,String Active){
        mID = ID;
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatTime = RepeatTime;
        mAddress = Address;
        mActive = Active;
    }

    public Reminder(String Title, String Date, String Time, String Repeat, String RepeatTime, String Address, String Active){
        mTitle = Title;
        mDate = Date;
        mTime = Time;
        mRepeat = Repeat;
        mRepeatTime = RepeatTime;
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

    public String getRepeatTime() {
        return mRepeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        mRepeatTime = repeatTime;
    }

    public String getRepeat() {
        return mRepeat;
    }

    public void setRepeat(String repeat) {
        mRepeat = repeat;
    }

    public String getActive() {
        return mActive;
    }

    public void setActive(String active) {
        mActive = active;
    }
}

