package com.example.acerth.helper;

import com.example.acerth.app.AppController;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.acerth.realrunner.R;

public class CustomListUserAdapterRanking extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<UserRanking> userRankingItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListUserAdapterRanking(Activity activity, List<UserRanking> userRankingItems) {
        this.activity = activity;
        this.userRankingItems = userRankingItems;
    }

    @Override
    public int getCount() {
        return userRankingItems.size();
    }

    @Override
    public Object getItem(int location) {
        return userRankingItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_user_ranking_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView numRow = (TextView)convertView.findViewById(R.id.numrow);
        TextView userName = (TextView)convertView.findViewById(R.id.user_name_rank);
        TextView sumDistance = (TextView)convertView.findViewById(R.id.user_score);
        TextView timeStart  = (TextView)convertView.findViewById(R.id.date);

        UserRanking u = userRankingItems.get(position);

        thumbNail.setImageUrl(String.valueOf(u.getUser_image_name()), imageLoader);
        numRow.setText(String.valueOf(u.getNum_row()));
        userName.setText(String.valueOf(u.getUser_game_name()));
        sumDistance.setText(String.valueOf(u.getSumDistance()));
        timeStart.setText(String.valueOf(u.getTimeStart()));

        return convertView;
    }


}
