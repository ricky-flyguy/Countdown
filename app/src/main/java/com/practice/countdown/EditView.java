package com.practice.countdown;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditView extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;

    private TextView date_result_textView;
    private TextView time_result_textView;
    private EditText name_input;

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
        name_input = (EditText)findViewById(R.id.name_textBox);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date_result_textView.setText(MainActivity.DATE_FORMAT.format(calendar.getTime()));

        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int _year, int _month, int _day) {
                        year = _year;
                        month = _month;
                        day = _day;
                        calendar.set(year, month, day);
                        date_result_textView.setText(MainActivity.DATE_FORMAT.format(calendar.getTime()));
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
                time_result_textView.setText(MainActivity.TIME_FORMAT.format(calendar.getTime()));
            }
        });
    }

    public void onConfirmClicked(View view) {

        calendar.set(year, month, day, hour, minute);

        final String id = "id000";
        final String name = name_input.getText().toString();
        final long endDate = calendar.getTimeInMillis();

        // Get set date & time from pickers
        AlertDialog.Builder alert = new AlertDialog.Builder(EditView.this);
        alert.setTitle("Please Confirm!");
        alert.setMessage("\nName: " + name_input.getText() + "\n\n"
            + "Date: " + MainActivity.FULL_DATE_FORMAT.format(calendar.getTime()));
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Create data for the specified date
                // Bundle data to be passed out of activity
                // Go back to home page

                CountdownData countdownData = new CountdownData(id, name, endDate);
                Intent intent = new Intent();
                intent.putExtra(DataManager.ID_TAG, countdownData.getId());
                intent.putExtra(DataManager.NAME_TAG, countdownData.getName());
                intent.putExtra(DataManager.END_DATE_TAG, countdownData.getEndTime());
                setResult(Activity.RESULT_OK, intent);
                dialogInterface.dismiss();
                finish();
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
