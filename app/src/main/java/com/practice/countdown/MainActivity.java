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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button opnEditBtn;
    Button openTimerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        opnEditBtn = findViewById(R.id.openEdit);
        opnEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, EditView.class);
                startActivity(intent);
            }
        });

        openTimerBtn = findViewById(R.id.openTimer);
        openTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, TimerView.class);
                startActivity(intent);
            }
        });
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
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

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] titles;
        String[] descs;
        int[] images;

        MyAdapter (Context context, String[] titles, String[] desc, int[] images) {
            super(context, R.layout.list_element, R.id.title_text, titles);
            this.context = context;
            this.titles = titles;
            this.descs = desc;
            this.images = images;
        }

        @NonNull
        @Override
        public View getView(int index, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listElement = layoutInflater.inflate(R.layout.list_element, parent, false);
            ImageView image = listElement.findViewById(R.id.image);
            TextView title = listElement.findViewById(R.id.title_text);
            TextView desc = listElement.findViewById(R.id.desc_text);

            // now we assign the specific values
            image.setImageResource(images[index]);
            title.setText(titles[index]);
            desc.setText(descs[index]);

            return listElement;
        }
    }
}