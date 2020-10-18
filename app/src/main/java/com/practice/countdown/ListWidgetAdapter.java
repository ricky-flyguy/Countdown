package com.practice.countdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListWidgetAdapter extends ArrayAdapter<CountdownData> {

    public enum Mode {
        VIEW,
        UTILITY
    }

    Context context;
    ViewGroup parent;
    Mode mode;

    View[] views;

    public ListWidgetAdapter(Context context, ArrayList<CountdownData> data, @NonNull ViewGroup parent) {
        super(context, R.layout.timer_list_widget, R.id.list_item_title, data);
        this.context = context;
        this.parent = parent;

        setMode(Mode.VIEW);
        setContent(data);
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public void setContent(ArrayList<CountdownData> data) {
        clear();
        addAll(data);
        inflateContent();
    }

    public void inflateContent() {
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listElement = null;

        views = new View[getCount()];

        for (int index = 0; index < views.length; ++index) {
            listElement = layoutInflater.inflate(R.layout.timer_list_widget, parent, false);
            TextView titleTextView = listElement.findViewById(R.id.list_item_title);
            TextView dateTextView = listElement.findViewById(R.id.timer_list_item_date);
            View option_layout = listElement.findViewById(R.id.timer_list_item_options_layout);

            titleTextView.setText(getItem(index).getName());
            dateTextView.setText(MainActivity.FULL_DATE_FORMAT.format(getItem(index).getDate()));

            option_layout.setVisibility(View.GONE);
            views[index] = listElement;
        }
    }

    @NonNull
    @Override
    public View getView(int index, @Nullable View convertView, @NonNull ViewGroup parent) {
        return views[index];
    }
}
