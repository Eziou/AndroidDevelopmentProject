package com.example.project;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_01";
    private RecyclerView mList;
    private UserAdapter mAdapter;
    private TextView mNoReminderView;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHelper userData;
    private int mTempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private AlarmReceiver mAlarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        userData = new DatabaseHelper(getApplicationContext());

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new UserAdapter();

        mList = (RecyclerView) findViewById(R.id.reminder_list);
        mNoReminderView = (TextView) findViewById(R.id.no_reminder_text);
        registerForContextMenu(mList);
        //mAdapter.setItemCount();
        //mAdapter.notifyDataSetChanged();
        mList.setLayoutManager(mLinearLayoutManager);
        mList.setAdapter(mAdapter);

        List<Reminder> mTest = userData.getAllReminders();

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        }
        mAlarmReceiver = new AlarmReceiver();
    }

    // On clicking a reminder item
    private void selectReminder(int mClickID) {
        String mStringClickID = Integer.toString(mClickID);

        // Create intent to edit the reminder
        // Put reminder id as extra
        Intent i = new Intent(this, ReminderEditActivity.class);
        i.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, mStringClickID);
        startActivityForResult(i, 1);
    }

    private int getDefaultItemCount() {
        return 100;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_reminder:{
                Intent intent=new Intent(this,ReminderAddActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.theme: {
                Toast.makeText(this, "Theme changed", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.language: {

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        private ArrayList<ReminderItem> mData;

        public UserAdapter() {
            mData = new ArrayList<>();
            ArrayList<ReminderItem> items = new ArrayList<>();
            List<Reminder> reminders = userData.getAllReminders();

            List<String> Titles = new ArrayList<>();
            List<String> Repeats = new ArrayList<>();
            List<String> RepeatTime = new ArrayList<>();
            List<String> Address = new ArrayList<>();
            List<String> Actives = new ArrayList<>();
            List<String> DateAndTime = new ArrayList<>();
            List<Integer> IDList= new ArrayList<>();

            for (Reminder r : reminders) {
                Titles.add(r.getTitle());
                DateAndTime.add(r.getDate() + " " + r.getTime());
                Repeats.add(r.getRepeat());
                RepeatTime.add(r.getRepeatTime());
                Address.add(r.getAddress());
                Actives.add(r.getActive());
                IDList.add(r.getID());
            }

            int k = 0;
            for(int i=0;i<reminders.size();i++){
                items.add(new UserAdapter.ReminderItem(Titles.get(i), DateAndTime.get(i), Repeats.get(i),
                        RepeatTime.get(i), Address.get(i), Actives.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
            mData = items;
        }

        /*public void setItemCount() {
            items.clear();
            items.addAll(mData);
            notifyDataSetChanged();
        }*/

        public void addItem(ReminderItem reminder){
            mData.add(reminder);
            notifyDataSetChanged();
        }

        public void removeItem(int customer){
            mData.remove(customer);
            notifyDataSetChanged();
        }

        public void setItemCount(ArrayList<ReminderItem> mItems) {
            mData.clear();
            mData.addAll(mItems);
            notifyDataSetChanged();
        }

        /*public void onDeleteItem(int count) {
            mData.clear();
            mData.addAll(generateData(count));
        }*/

        public void removeItemSelected(int selected) {
            if (mData.isEmpty()) return;
            mData.remove(selected);
            notifyItemRemoved(selected);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);

            return new ViewHolder(view, this);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ReminderItem item = mData.get(position);
            holder.setReminderTitle(item.mTitle);
            holder.setReminderTime(item.mDateTime);
//            holder.setReminderRepeat(item.mRepeat, item.mRepeatTime);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ReminderItem {
            public String mTitle;
            public String mDateTime;
            public String mRepeat;
            public String mRepeatTime;
            public String mAddress;
            public String mActive;

            public ReminderItem(String Title, String DateTime, String Repeat, String RepeatTime, String Address, String Active) {
                this.mTitle = Title;
                this.mDateTime = DateTime;
                this.mRepeat = Repeat;
                this.mRepeatTime = RepeatTime;
                this.mAddress = Address;
                this.mActive = Active;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mTitle;
            private UserAdapter mAdapter;
            private TextView mTime, mRepeat;

            public ViewHolder(View itemView, UserAdapter adapter) {
                super(itemView);
                itemView.setOnClickListener(this);

                mAdapter = adapter;
                mTitle = (TextView) itemView.findViewById(R.id.recycle_title);
                mTime = (TextView) itemView.findViewById(R.id.recycle_date_time);
                mRepeat = (TextView) itemView.findViewById(R.id.recycle_repeat_info);
            }

            @Override
            public void onClick(View view) {
                mTempPost = mList.getChildAdapterPosition(view);

                int mReminderClickID = IDmap.get(mTempPost);
                selectReminder(mReminderClickID);
            }

            public void setReminderTitle(String title) {
                mTitle.setText(title);
            }

            public void setReminderTime(String time) {
                mTime.setText(time);
            }

/*            public void setReminderRepeat(String repeat, String RepeatTime) {
                if(repeat.equals("true")){
                    mRepeat.setText("Every " + RepeatTime + " Days " + "(s)");
                }else if (repeat.equals("false")) {
                    mRepeat.setText("Repeat Off");*/
//                }
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}