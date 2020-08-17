package com.practice.countdown;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditView extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;

    private TextView date_result_textView;
    private TextView time_result_textView;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, d, y");
    final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    final SimpleDateFormat fullFormat = new SimpleDateFormat("MMM, d, y @ h:mm a");

    Calendar calendar;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_timer_view);

        datePicker = (DatePicker)findViewById(R.id.date_picker);
        timePicker = (TimePicker)findViewById(R.id.time_picker);
        date_result_textView = (TextView)findViewById(R.id.date_result);
        time_result_textView = (TextView)findViewById(R.id.time_result);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date_result_textView.setText(dateFormat.format(calendar.getTime()));

        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int _year, int _month, int _day) {
                        year = _year;
                        month = _month;
                        day = _day;
                        calendar.set(year, month, day);
                        date_result_textView.setText(dateFormat.format(calendar.getTime()));
                    }
                });

        hour = 00;
        minute = 00;
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int _hour, int _minute) {
                hour = _hour;
                minute = _minute;
                second = 00;
                calendar.setTime(MainActivity.getTime(0, 0, 0, hour, minute, second));
                time_result_textView.setText(timeFormat.format(calendar.getTime()));
            }
        });
    }

    public void onConfirmClicked(View view) {

        // Get set date & time from pickers
        calendar.set(year, month, day, hour, minute);

        AlertDialog.Builder alert = new AlertDialog.Builder(EditView.this);
        alert.setTitle("Confirm changes!");
        alert.setMessage("Set timer for " + fullFormat.format(calendar.getTime()) + " ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                // Create data for the specified date
                // Bundle data to be passed out of activity
                // Go back to home page
                dialogInterface.dismiss();
            }
        });

        alert.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.show();
    }
}
