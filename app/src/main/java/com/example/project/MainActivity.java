package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mList;
    //private OneAdapter mAdapter;
    private  MyAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //mAdapter = new OneAdapter();
        mAdapter = new MyAdapter(getData());

        mList = (RecyclerView) findViewById(R.id.reminder_list);
        mList.setLayoutManager(mLinearLayoutManager);
        mList.setAdapter(mAdapter);
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

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        String temp = " item";
        for(int i = 0; i < 20; i++) {
            data.add(i + temp);
        }

        return data;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private ArrayList<String> mData;

        public MyAdapter(ArrayList<String> data) {
            this.mData = data;
        }

        public void updateData(ArrayList<String> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // 绑定数据
            holder.mTv.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mTv = (TextView) itemView.findViewById(R.id.recycle_title);
            }
        }
    }

    /*public class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder> {

        private final ArrayList<ReminderItem> items;

        public OneAdapter() {
            items = new ArrayList<>();
        }

        *//*public void setItemCount(int count) {
            items.clear();
            items.addAll(generateData(count));
            notifyDataSetChanged();
        }

        public void onDeleteItem(int count) {
            items.clear();
            //items.addAll(generateData(count));
            items.addAll(generateData(count));
        }

        public void removeItemSelected(int selected) {
            if (items.isEmpty()) return;
            items.remove(selected);
            notifyItemRemoved(selected);
        }*//*

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull OneAdapter.ViewHolder holder, int position) {
            ReminderItem item = items.get(position);
            holder.setReminderTitle(item.mTitle);
            holder.setReminderDateTime(item.mDateTime);
            holder.setReminderRepeatInfo(item.mRepeat, item.mRepeatNo, item.mRepeatType);
            // holder.setActiveImage(item.mActive);
        }

        @Override
        public int getItemCount() {
            //return items.size();
            return mData == null ? 0 : mData.size();
        }

        public class ReminderItem {
            public String mTitle;
            public String mDateTime;
            public String mRepeat;
            public String mRepeatNo;
            public String mRepeatType;
            public String mActive;

            public ReminderItem(String Title, String DateTime, String Repeat, String RepeatNo, String RepeatType, String Active) {
                this.mTitle = Title;
                this.mDateTime = DateTime;
                this.mRepeat = Repeat;
                this.mRepeatNo = RepeatNo;
                this.mRepeatType = RepeatType;
                this.mActive = Active;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;;

            public ViewHolder(View itemView) {
                super(itemView);
                mTitleText = (TextView) itemView.findViewById(R.id.recycle_title);
                mDateAndTimeText = (TextView) itemView.findViewById(R.id.recycle_date_time);
            }

            public void setReminderTitle(String title) {
                mTitleText.setText(title);
                String letter = "A";

                if(title != null && !title.isEmpty()) {
                    letter = title.substring(0, 1);
                }

                // int color = mColorGenerator.getRandomColor();

                // Create a circular icon consisting of  a random background colour and first letter of title
                *//*mDrawableBuilder = TextDrawable.builder()
                        .buildRound(letter, color);
                mThumbnailImage.setImageDrawable(mDrawableBuilder);*//*
            }

            public void setReminderDateTime(String datetime) {
                mDateAndTimeText.setText(datetime);
            }

            public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
                if(repeat.equals("true")){
                    mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
                }else if (repeat.equals("false")) {
                    mRepeatInfoText.setText("Repeat Off");
                }
            }

            *//*public List<ReminderItem> generateData(int count) {
                return "1234";
                ArrayList<SimpleAdapter.ReminderItem> items = new ArrayList<>();

                // Get all reminders from the database
                List<Reminder> reminders = rb.getAllReminders();

                // Initialize lists
                List<String> Titles = new ArrayList<>();
                List<String> Repeats = new ArrayList<>();
                List<String> RepeatNos = new ArrayList<>();
                List<String> RepeatTypes = new ArrayList<>();
                List<String> Actives = new ArrayList<>();
                List<String> DateAndTime = new ArrayList<>();
                List<Integer> IDList= new ArrayList<>();
                List<DateTimeSorter> DateTimeSortList = new ArrayList<>();

                // Add details of all reminders in their respective lists
                for (Reminder r : reminders) {
                    Titles.add(r.getTitle());
                    DateAndTime.add(r.getDate() + " " + r.getTime());
                    Repeats.add(r.getRepeat());
                    RepeatNos.add(r.getRepeatNo());
                    RepeatTypes.add(r.getRepeatType());
                    Actives.add(r.getActive());
                    IDList.add(r.getID());
                }

                int key = 0;

                // Add date and time as DateTimeSorter objects
                for(int k = 0; k<Titles.size(); k++){
                    DateTimeSortList.add(new DateTimeSorter(key, DateAndTime.get(k)));
                    key++;
                }

                // Sort items according to date and time in ascending order
                Collections.sort(DateTimeSortList, new DateTimeComparator());

                int k = 0;

                // Add data to each recycler view item
                for (DateTimeSorter item:DateTimeSortList) {
                    int i = item.getIndex();

                    items.add(new SimpleAdapter.ReminderItem(Titles.get(i), DateAndTime.get(i), Repeats.get(i),
                            RepeatNos.get(i), RepeatTypes.get(i), Actives.get(i)));
                    IDmap.put(k, IDList.get(i));
                    k++;
                }
                return items;
            }*//*
        }
    }*/

}