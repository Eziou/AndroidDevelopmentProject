package com.example.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class ReminderAddActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView setDate = (TextView) findViewById(R.id.set_date);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("setReminder_title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    public void set_Date(View view) {
    }
}