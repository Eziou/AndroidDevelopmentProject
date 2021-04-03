package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.text.BreakIterator;
import java.util.Calendar;

public class ReminderAddActivity extends AppCompatActivity
        //implements LoaderManager.LoaderCallbacks<Cursor>
        //implements
        //TimePickerDialog.OnTimeSetListener,
        //DatePickerDialog.OnDateSetListener
{

    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar cal;
    //private Toolbar toolbar;
    private TextView setTitle, setDate, setTime, setRepeat, repeatTime, setAddress, setContact, mRepeatText;
    private String mDate, mTime, mTitle, mActive;
    private String mRepeat, mRepeatTime;;
    private DatePickerDialog.OnDateSetListener onDate;
    private TimePickerDialog.OnTimeSetListener onTime;
    //private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    //private static final int LOADER_ID=1976;
    //private static final int PICK_CONTACT=122;
    //private SimpleCursorAdapter adapter;
    //private ListView listView;

    //final String[] from =   new String[]{ContactsContract.Contacts.DISPLAY_NAME};
    // ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME};

    //final int[] to = new int[] { R.id.name};
    //, R.id.address };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        //toolbar = findViewById(R.id.toolbar);
        setTitle = findViewById(R.id.title);
        setDate = findViewById(R.id.set_date);
        setTime = findViewById(R.id.set_time);
        setRepeat = findViewById(R.id.set_repeat);
        repeatTime = findViewById(R.id.set_repeat_time);
        // repeatType = findViewById(R.id.set_repeat_type);
        setAddress = findViewById(R.id.set_address);
        setContact = findViewById(R.id.set_contact);

        /*getSupportActionBar().setTitle("setReminder_title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setSupportActionBar(toolbar);*/

        cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH) + 1;
        mDay = cal.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        //adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, null, from, to, 0);
        //listView.setAdapter(adapter);

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
        onDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                setDate.setText(new StringBuilder().append(mMonth+1). append("-").append(mDay).append("-").append(mYear).append(" "));
            }
        };

        onTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                setTime.setText(new StringBuilder().append(mHour). append(":").append(mMinute));
            }
        };

        //requestPermissions();
    }

    /*public void requestPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
        {
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission is granted
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                Toast.makeText(this, "You must grant permission to display contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

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

    /*public void set_Contact(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }*/

    /*@Override public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == MainActivity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

            *//*String hasPhone =
              c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1"))
            {
              Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
              phones.moveToFirst();
              String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
              // Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
              setCn(cNumber);
            }*//*
                    }
                }
        }
    }*/

    // turn on/off repeat
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText("Every " + mRepeatTime + " Days " + "(s)");
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    // set repeat interval
    public void setRepeatTime(View view) {
    }

    // click save button
    public void onSaveClick(View view) {
        DatabaseHelper userData = new DatabaseHelper(this);

        // Creating Reminder
        userData.addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatTime, mActive));

        // Set up calender for creating the notification
        cal.set(Calendar.MONTH, --mMonth);
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.DAY_OF_MONTH, mDay);
        cal.set(Calendar.HOUR_OF_DAY, mHour);
        cal.set(Calendar.MINUTE, mMinute);
        cal.set(Calendar.SECOND, 0);

        // Create a new notification
        /*if (Active.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), cal, ID, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmReceiver().setAlarm(getApplicationContext(), cal, ID);
            }
        }*/

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    // click cancel button
    public void onCancelClick(View view) {
        finish();
        //super.onBackPressed();
    }

    /*@NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = ContactsContract.Data.MIMETYPE + " = ?";
        String[] selectionArgs = new String[]{ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};

        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME}
                ,selection, selectionArgs, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }*/
}