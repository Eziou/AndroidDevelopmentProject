package com.example.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar cal;
    private Toolbar toolbar;
    private TextView setTitle, setDate, setTime, setRepeat, repeatNo, repeatType, setAddress, setContact;
    private int year, month, hour, minute, day;
    private String Date, Time, Title;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        toolbar = findViewById(R.id.toolbar);
        setTitle = findViewById(R.id.title);
        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        setRepeat = findViewById(R.id.set_repeat);
        repeatNo = findViewById(R.id.set_repeat_no);
        repeatType = findViewById(R.id.set_repeat_type);
        setAddress = findViewById(R.id.set_address);
        setContact = findViewById(R.id.set_contact);

        /*getSupportActionBar().setTitle("setReminder_title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(toolbar);*/

        cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DATE);

        Date = day + "/" + month + "/" + year;
        Time = hour + ":" + minute;

        setTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Title = s.toString().trim();
                setTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void set_Date(View view) {
    }

    public void set_Time(View view) {
    }

    public void set_Address(View view) {
    }

    public void set_Contact(View view) {
    }

    public void setRepeatNo(View view) {
    }

    public void selectRepeatType(View view) {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}