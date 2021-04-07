package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.text.BreakIterator;
import java.util.Calendar;

public class ReminderAddActivity extends AppCompatActivity {

    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar cal;
    private TextView setTitle, setDate, setTime, repeatTime, setAddress, setContact, mRepeatText, mRepeatTimeText;
    private String mDate, mTime, mTitle, mActive;
    private String mRepeat, mRepeatTime, mAddress;
    private DatePickerDialog.OnDateSetListener onDate;
    private TimePickerDialog.OnTimeSetListener onTime;
    private long mRepeatLast;
    private static final long milDay = 86400000L;
    static String CHANNEL_ID= "channel_01";
    static int NOTIFICATION_ID=100;
    static int REQUEST_CODE= 200;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        setTitle = findViewById(R.id.title);
        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        mRepeatText = findViewById(R.id.set_repeat);
        repeatTime = findViewById(R.id.set_repeat_time);
        setAddress = findViewById(R.id.set_address);
        //setContact = findViewById(R.id.set_contact);

        cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH) + 1;
        mDay = cal.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        mActive = "true";
        mRepeat = "true";

        //adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, null, from, to, 0);
        //listView.setAdapter(adapter);

        //CreateNotificationChannel();
        setTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                setTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
                setDate.setText(new StringBuilder().append(mMonth+1). append("-").append(mDay).append("-").append(mYear).append(" "));
                mDate = mDay + "/" + (mMonth+1) + "/" + mYear;
            }
        };

        onTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                setTime.setText(new StringBuilder().append(mHour). append(":").append(mMinute));
                if (minute < 10) {
                    mTime = mHour + ":" + "0" + mMinute;
                } else {
                    mTime = mHour + ":" + mMinute;
                }
            }
        };
        //requestPermissions();
    }

    // click DatePicker and get date
    public void set_Date(View view) {
        DatePickerFragment date= new DatePickerFragment();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Bundle args = new Bundle();
        args.putInt("year",mYear);
        args.putInt("month",mMonth);
        args.putInt("day",mDay);

        date.setArguments(args);
        date.setCallBack(onDate);
        date.show(getSupportFragmentManager(),"Date Picker");
    }

    // click TimePicker and get time
    public void set_Time(View view) {
        TimePickerFragment time= new TimePickerFragment();

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        Bundle args = new Bundle();
        args.putInt("year",mHour);
        args.putInt("month",mMinute);

        time.setArguments(args);
        time.setCallBack(onTime);
        time.show(getSupportFragmentManager(),"Time Picker");
    }

    // On clicking the repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText(R.string.repeat_on);
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    // set repeat interval
    public void setRepeatTime(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatTime = Integer.toString(1);
                            //mRepeatTimeText.setText(mRepeatTime);
                            repeatTime.setText("Every " + mRepeatTime + " Day " + "(s)");
                        }
                        else {
                            mRepeatTime = input.getText().toString().trim();
                            //mRepeatTimeText.setText(mRepeatTime);
                            repeatTime.setText("Every " + mRepeatTime + " Day " + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // open address with Google Maps
    @SuppressLint("QueryPermissionsNeeded")
    public void launchMaps(View view) {
        String map = "http://maps.google.co.in/maps?q=" + setAddress.getText() ;
        Uri gmmIntentUri = Uri.parse(map);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        // After adding resolveActivity(getPackageManager(), cannot launch Google Maps
        //if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        //}

    }

    // click save button
    public void onSaveClick(View view) {
        DatabaseHelper userData = new DatabaseHelper(this);

        // Creating Reminder
        int ID = userData.addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatTime, mAddress, mActive));

        //userData.addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatTime, mAddress, mActive));
        // Set up calender for creating the notification
        cal.set(Calendar.MONTH, --mMonth);
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.HOUR_OF_DAY, mHour);
        cal.set(Calendar.MINUTE, mMinute);
        cal.set(Calendar.SECOND, 0);

        mRepeatLast = Integer.parseInt(mRepeatTime) * milDay;

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), cal, ID, mRepeatLast);
            } else if (mRepeat.equals("false")) {
                new AlarmReceiver().setAlarm(getApplicationContext(), cal, ID);
            }
        }
        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

        //onBackPressed();
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