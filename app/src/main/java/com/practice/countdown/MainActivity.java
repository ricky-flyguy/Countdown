package com.practice.countdown;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    Button addBtn;

    Button opnEditBtn;
    Button openTimerBtn;
    CountdownData dataArray[];

    DataManager dataManager;
    public final static SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("MMM, d, y @ h:mm a");
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM, d, y");
    public final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        addBtn = findViewById(R.id.add_button);

        opnEditBtn = findViewById(R.id.openEdit);
        openTimerBtn = findViewById(R.id.openTimer);

        dataManager = new DataManager(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, EditView.class);
                startActivity(intent);
            }
        });

        opnEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataManager.Save();
            }
        });

        openTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, TimerView.class);
                startActivity(intent);
            }
        });

        dataManager.Load();

        dataArray = dataManager.getDataList().toArray(new CountdownData[dataManager.getDataList().size()]);
        CounterWidgetAdapter adapter = new CounterWidgetAdapter(this, dataArray);
        listView.setAdapter(adapter);
    }

    public static Date getTime(int year, int month, int day, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    class CounterWidgetAdapter extends ArrayAdapter<CountdownData> {

        CountdownData data[];
        Context context;

        CounterWidgetAdapter (Context context, CountdownData[] data) {
            super(context, R.layout.timer_list_view, R.id.timer_list_item_title, data);
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public View getView(int index, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listElement = layoutInflater.inflate(R.layout.timer_list_view, parent, false);
            TextView titleTextView = listElement.findViewById(R.id.timer_list_item_title);
            TextView dateTextView = listElement.findViewById(R.id.timer_list_item_date);

            titleTextView.setText(data[index].getName());
            dateTextView.setText(MainActivity.FULL_DATE_FORMAT.format(data[index].getDate()));

            return listElement;
        }
    }
}