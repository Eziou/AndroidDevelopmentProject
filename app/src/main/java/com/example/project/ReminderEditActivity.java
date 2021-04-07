package com.example.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class ReminderEditActivity extends AppCompatActivity {

    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar cal;
    private TextView setTitle, setDate, setTime, repeatTime, setAddress, mRepeatText;
    private String mDate, mTime, mTitle, mActive;
    private String mRepeat, mRepeatTime, mAddress;
    private DatePickerDialog.OnDateSetListener onDate;
    private TimePickerDialog.OnTimeSetListener onTime;
    private long mRepeatLast;
    private int mReceivedID;
    private Reminder mReceivedReminder;
    private DatabaseHelper userData;
    private Switch setActive, setRepeat;
    private AlarmReceiver mAlarmReceiver;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";
    private static final long milDay = 86400000L;

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
        setActive = findViewById(R.id.active_switch);
        setRepeat = findViewById(R.id.repeat_switch);

        mReceivedID = Integer.parseInt(getIntent().getStringExtra(EXTRA_REMINDER_ID));
        userData = new DatabaseHelper(this);
        mReceivedReminder = userData.getReminder(mReceivedID);

        mTitle = mReceivedReminder.getTitle();
        mDate = mReceivedReminder.getDate();
        mTime = mReceivedReminder.getTime();
        mRepeat = mReceivedReminder.getRepeat();
        mRepeatTime = mReceivedReminder.getRepeatTime();
        mAddress = mReceivedReminder.getAddress();
        mActive = mReceivedReminder.getActive();

        setTitle.setText(mTitle);
        setDate.setText(mDate);
        setTime.setText(mTime);
        setAddress.setText(mAddress);
        repeatTime.setText("Every " + mRepeatTime + " Day(s)");

        if (mActive.equals("false")) {
            setActive.setChecked(false);
        } else if (mActive.equals("true")) {
            setActive.setChecked(true);
        }

        if (mRepeat.equals("false")) {
            setRepeat.setChecked(false);
            mRepeatText.setText(R.string.repeat_off);

        } else if (mRepeat.equals("true")) {
            setRepeat.setChecked(true);
            mRepeatText.setText(R.string.repeat_on);
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
    public void setRepeatTime(View v) {
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
                            repeatTime.setText("Every " + mRepeatTime + " Day " + "(s)");
                        } else {
                            mRepeatTime = input.getText().toString().trim();
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
        mReceivedReminder.setDate(mDate);
        mReceivedReminder.setTime(mTime);
        mReceivedReminder.setRepeat(mRepeat);
        mReceivedReminder.setRepeatTime(mRepeatTime);
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
        mRepeatLast = Integer.parseInt(mRepeatTime) * milDay;

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                mAlarmReceiver.setRepeatAlarm(getApplicationContext(), cal, mReceivedID, mRepeatLast);
            } else if (mRepeat.equals("false")) {
                mAlarmReceiver.setAlarm(getApplicationContext(), cal, mReceivedID);
            }
        }

        // Create toast to confirm update
        Toast.makeText(getApplicationContext(), "Edited",
                Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /*private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Notification channel description");
            // register the channel
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Titre")
                .setContentText("Contenu")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        // notificationId : unique identifier to define
        notificationManager.notify(NOTIFICATION_ID, notifBuilder.build());
    }*/

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

