package com.example.acerth.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acerth.realrunner.R;

import java.util.List;

public class CustomListAdapterHistoryOfUser extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<HistoryOfUser> historyOfUserItems;


    public CustomListAdapterHistoryOfUser(Activity activity, List<HistoryOfUser> historyOfUserItems) {
        this.activity = activity;
        this.historyOfUserItems = historyOfUserItems;
    }

    @Override
    public int getCount() {
        return historyOfUserItems.size();
    }

    @Override
    public Object getItem(int location) {
        return historyOfUserItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_history_row, null);

        TextView time_start = (TextView)convertView.findViewById(R.id.time_start);
        TextView time_stop = (TextView)convertView.findViewById(R.id.time_stop);
        TextView elapsedTime = (TextView)convertView.findViewById(R.id.time_total);
        TextView distance = (TextView)convertView.findViewById(R.id.distance);
        TextView calories = (TextView)convertView.findViewById(R.id.cal);
        TextView step = (TextView)convertView.findViewById(R.id.stepVal);

        HistoryOfUser his = historyOfUserItems.get(position);

        time_start.setText(String.valueOf(his.getTime_start()));
        time_stop.setText(String.valueOf(his.getTime_stop()));
        elapsedTime.setText(String.valueOf(his.getElapsedTime()));
        distance.setText(String.valueOf(his.getDistance()));
        calories.setText(String.valueOf(his.getCalories()));
        step.setText(String.valueOf(his.getStep()));

        return convertView;
    }
}
