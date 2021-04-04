package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mList;
    private UserAdapter mAdapter;
    private TextView mNoReminderView;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHelper userData;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = new DatabaseHelper(getApplicationContext());

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new UserAdapter();

        mList = (RecyclerView) findViewById(R.id.reminder_list);
        mNoReminderView = (TextView) findViewById(R.id.no_reminder_text);
        // registerForContextMenu(mList);
        mList.setLayoutManager(mLinearLayoutManager);
        mList.setAdapter(mAdapter);

        List<Reminder> mTest = userData.getAllReminders();

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if (item.getItemId()==R.id.delete){
            // myHelper.delete(info.id);
            // chargeData();
            return true;
        }
        return super.onContextItemSelected(item);
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
            case R.id.search: {
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        private ArrayList<ReminderItem> mData;

        /*public UserAdapter(ArrayList<ReminderItem> data) {
            this.mData = data;
        }*/
        public UserAdapter() {
            mData = new ArrayList<>();
            ArrayList<UserAdapter.ReminderItem> items = new ArrayList<>();
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
            holder.setReminderRepeat(item.mRepeat, item.mRepeatTime);
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
            public void onClick(View v) {

            }

            public void setReminderTitle(String title) {
                mTitle.setText(title);
            }

            public void setReminderTime(String time) {
                mTime.setText(time);
            }

            public void setReminderRepeat(String repeat, String RepeatTime) {
                if(repeat.equals("true")){
                    mRepeat.setText("Every " + RepeatTime + " Days " + "(s)");
                }else if (repeat.equals("false")) {
                    mRepeat.setText("Repeat Off");
                }
            }
        }
    }
}