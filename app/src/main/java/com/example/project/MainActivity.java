package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private RecyclerView mList;
    private UserAdapter mAdapter;
    private TextView mNoReminderView;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHelper userData;

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

    public static class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

        private final ArrayList<ReminderItem> mData;

        public UserAdapter() {
            mData = new ArrayList<>();
        }

        /*public void setItemCount(int count) {
            mData.clear();
            mData.addAll(generateData(count));
            notifyDataSetChanged();
        }*/

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);

            // ViewHolder viewHolder = new ViewHolder(v);
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

        public static class ReminderItem {
            public String mTitle;
            public String mDateTime;
            public String mRepeat;
            public String mRepeatTime;
            //public String mRepeatType;
            public String mActive;

            public ReminderItem(String Title, String DateTime, String Repeat, String RepeatTime, String Active) {
                this.mTitle = Title;
                this.mDateTime = DateTime;
                this.mRepeat = Repeat;
                this.mRepeatTime = RepeatTime;
                //this.mRepeatType = Repeat;
                this.mActive = Active;
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

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