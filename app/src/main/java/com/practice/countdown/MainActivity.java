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

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button opnEditBtn;
    Button openTimerBtn;
    CountdownData data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        opnEditBtn = findViewById(R.id.openEdit);
        openTimerBtn = findViewById(R.id.openTimer);

        opnEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, EditView.class);
                startActivity(intent);
            }
        });

        openTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, TimerView.class);
                startActivity(intent);
            }
        });

        data = new CountdownData[10];
        CounterWidgetAdapter adapter = new CounterWidgetAdapter(this, data);
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

        MyAdapter (Context context, String[] titles, String[] desc, int[] images) {
            super(context, R.layout.list_element, R.id.title_text, titles);
            this.context = context;
            this.data = data;
        }

        @NonNull
        @Override
        public View getView(int index, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listElement = layoutInflater.inflate(R.layout.timer_list_view, parent, false);
            TextView titleTextView = listElement.findViewById(R.id.timer_list_item_title);
            TextView dateTextView = listElement.findViewById(R.id.timer_list_item_remaining);


            return listElement;
        }
    }
}