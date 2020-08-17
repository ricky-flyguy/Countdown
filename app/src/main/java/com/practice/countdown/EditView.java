package com.practice.countdown;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditView extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;

    private TextView date_result_textView;
    private TextView time_result_textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_timer_view);

        datePicker = (DatePicker)findViewById(R.id.date_picker);
        timePicker = (TimePicker)findViewById(R.id.time_picker);
        date_result_textView = (TextView) findViewById(R.id.date_result);
        time_result_textView = (TextView) findViewById(R.id.time_result);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, d, y");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("h:m a");

        datePicker.init(year, month, day,
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        date_result_textView.setText(dateFormat.format(calendar.getTime()));
                    }
                });

        Date date = new Date();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {

                boolean isPM = false;
                calendar.setTime(MainActivity.getTime(0, 0, 0, hour, minute, 0));
                time_result_textView.setText(timeFormat.format(calendar.getTime()));
            }
        });


    }
}
