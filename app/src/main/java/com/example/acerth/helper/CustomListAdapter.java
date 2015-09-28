package com.example.acerth.helper;

import com.example.acerth.app.AppController;
import com.example.acerth.helper.User;

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

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> userItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<User> userItems) {
        this.activity = activity;
        this.userItems = userItems;
    }

    @Override
    public int getCount() {
        return userItems.size();
    }

    @Override
    public Object getItem(int location) {
        return userItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_user_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView userID = (TextView)convertView.findViewById(R.id.user_id);
        TextView userName = (TextView)convertView.findViewById(R.id.user_name);

        User u = userItems.get(position);

        thumbNail.setImageUrl(String.valueOf(u.getThumbnailUrl()), imageLoader);
        userID.setText(String.valueOf(u.getUser_id()));
        userName.setText(String.valueOf(u.getUser_game_name()));

        return convertView;
    }


}
