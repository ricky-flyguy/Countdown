package com.practice.countdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button addBtn;
    private Button opnEditBtn;
    private Button openTimerBtn;
    private ListWidgetAdapter adapter;
    private ViewGroup listViewParent;

    private DataManager dataManager;
    public final static SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("MMM, d, y @ h:mm a");
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM, d, y");
    public final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");
    public final int LAUNCH_EDITVIEW_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        addBtn = findViewById(R.id.add_button);
        opnEditBtn = findViewById(R.id.openEdit);
        openTimerBtn = findViewById(R.id.openTimer);
        listViewParent = findViewById(R.id.main);

        dataManager = new DataManager(this);
        dataManager.Load();

        adapter = new ListWidgetAdapter(this, new ArrayList<CountdownData>(), listViewParent);
        refreshListView();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditView.class);
                startActivityForResult(intent, LAUNCH_EDITVIEW_ACTIVITY);
                //startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, TimerView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_EDITVIEW_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String id = data.getStringExtra(DataManager.ID_TAG);
                String name = data.getStringExtra(DataManager.NAME_TAG);
                long endDate = data.getLongExtra(DataManager.END_DATE_TAG, 0);

                CountdownData cData = new CountdownData(id, name, endDate);
                dataManager.addData(cData);
                dataManager.Save();

                refreshListView();
            }
            if (requestCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void refreshListView() {
        // fill the dataArray with info stored in our dataManager
        // reset adapter reference with new array info
        // reset the list Views adapter since we created a new reference
        adapter.setContent(dataManager.getDataList());
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
}

