package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class ReminderEditActivity extends AppCompatActivity {

    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar cal;
    private TextView setTitle, setDate, setTime, setAddress, setContact;
    private String mDate, mTime, mTitle, mActive, mContact;
    private String mAddress;
    private DatePickerDialog.OnDateSetListener onDate;
    private TimePickerDialog.OnTimeSetListener onTime;
    private int mReceivedID;
    private Reminder mReceivedReminder;
    private DatabaseHelper userData;
    private Switch setActive;
    private AlarmReceiver mAlarmReceiver;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";
    private static final long milDay = 86400000L;
    private static final int LOADER_ID=1976;
    private static final int PICK_CONTACT=122;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main_setting);

        setTitle = findViewById(R.id.title);
        setContact = findViewById(R.id.set_contact);
        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        setAddress = findViewById(R.id.set_address);
        setActive = findViewById(R.id.active_switch);

        mReceivedID = Integer.parseInt(getIntent().getStringExtra(EXTRA_REMINDER_ID));
        userData = new DatabaseHelper(this);
        mReceivedReminder = userData.getReminder(mReceivedID);

        mTitle = mReceivedReminder.getTitle();
        mContact = mReceivedReminder.getContact();
        mDate = mReceivedReminder.getDate();
        mTime = mReceivedReminder.getTime();
        mAddress = mReceivedReminder.getAddress();
        mActive = mReceivedReminder.getActive();

        setTitle.setText(mTitle);
        setContact.setText(mContact);
        setDate.setText(mDate);
        setTime.setText(mTime);
        setAddress.setText(mAddress);

        if (mActive.equals("false")) {
            setActive.setChecked(false);
        } else if (mActive.equals("true")) {
            setActive.setChecked(true);
        }

        cal = Calendar.getInstance();
        mAlarmReceiver = new AlarmReceiver();

        mDateSplit = mDate.split("/");
        mTimeSplit = mTime.split(":");

        mDay = Integer.parseInt(mDateSplit[0]);
        mMonth = Integer.parseInt(mDateSplit[1]);
        mYear = Integer.parseInt(mDateSplit[2]);
        mHour = Integer.parseInt(mTimeSplit[0]);
        mMinute = Integer.parseInt(mTimeSplit[1]);

        setTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                setTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAddress = s.toString().trim();
                setAddress.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        onDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                setDate.setText(new StringBuilder().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
                mDate = mDay + "/" + (mMonth+1) + "/" + mYear;
            }
        };

        onTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                setTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute));
                if (minute < 10) {
                    mTime = mHour + ":" + "0" + mMinute;
                } else {
                    mTime = mHour + ":" + mMinute;
                }
            }
        };
        requestPermissions();
    }
    public void requestPermissions()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission is granted
            } else {
                Toast.makeText(this, "You must grant permission to display contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //click contacts and get a contact
    public void set_contact(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactURI = data.getData();
                Cursor cursor = getContentResolver().query(contactURI, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
                    setContact.setText(name);
                    mContact = name;
                }
                cursor.close();
            }
        }
    }

    // click DatePicker and get date
    public void set_Date(View view) {
        DatePickerFragment date = new DatePickerFragment();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Bundle args = new Bundle();
        args.putInt("year", mYear);
        args.putInt("month", mMonth);
        args.putInt("day", mDay);

        date.setArguments(args);
        date.setCallBack(onDate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    // click TimePicker and get time
    public void set_Time(View view) {
        TimePickerFragment time = new TimePickerFragment();

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        Bundle args = new Bundle();
        args.putInt("year", mHour);
        args.putInt("month", mMinute);

        time.setArguments(args);
        time.setCallBack(onTime);
        time.show(getSupportFragmentManager(), "Time Picker");
    }

    // open address with Google Maps
    @SuppressLint("QueryPermissionsNeeded")
    public void launchMaps(View view) {
        String map = "http://maps.google.co.in/maps?q=" + setAddress.getText();
        Uri gmmIntentUri = Uri.parse(map);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        // After adding resolveActivity(getPackageManager(), cannot launch Google Maps
        //if (mapIntent.resolveActivity(getPackageManager()) != null) {
        startActivity(mapIntent);
        //}
    }

    public void onSaveClick(View view){
        // Set new values in the reminder
        mReceivedReminder.setTitle(mTitle);
        mReceivedReminder.setContact(mContact);
        mReceivedReminder.setDate(mDate);
        mReceivedReminder.setTime(mTime);
        mReceivedReminder.setAddress(mAddress);
        mReceivedReminder.setActive(mActive);

        // Update reminder
        userData.updateReminder(mReceivedReminder);

        // Set up calender for creating the notification
        cal.set(Calendar.MONTH, --mMonth);
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.HOUR_OF_DAY, mHour);
        cal.set(Calendar.MINUTE, mMinute);
        cal.set(Calendar.SECOND, 0);

        // Cancel existing notification of the reminder by using its ID
        mAlarmReceiver.cancelAlarm(getApplicationContext(), mReceivedID);

        // Create a new notification
        if (mActive.equals("true")) {
                mAlarmReceiver.setAlarm(getApplicationContext(), cal, mReceivedID);
        }

        // Create toast to confirm update
        Toast.makeText(getApplicationContext(), "Edited",
                Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    // click cancel button
    public void onCancelClick(View view) {
        finish();
        //super.onBackPressed();
    }

    public void onSwitchActive(View view) {
        boolean active = ((Switch) view).isChecked();
        if (active) {
            mActive = "true";
            //mRepeatText.setText(R.string.repeat_on);
        } else {
            mActive = "false";
            //mRepeatText.setText(R.string.repeat_off);
        }
    }
}

