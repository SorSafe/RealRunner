package com.example.acerth.helper;

import com.example.acerth.app.AppController;
import com.example.acerth.helper.HelperHealth;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.acerth.realrunner.R;


public class HealthListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<HelperHealth> healthItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public HealthListAdapter(Activity activity, List<HelperHealth> healthItems) {
        this.activity = activity;
        this.healthItems = healthItems;
    }

    @Override
    public int getCount() {
        return healthItems.size();
    }

    @Override
    public Object getItem(int location) {
        return healthItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_health_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView healthPic = (NetworkImageView) convertView.findViewById(R.id.healthPic);
        TextView healthTopic = (TextView)convertView.findViewById(R.id.healthTopic);
        //TextView healthDesc = (TextView)convertView.findViewById(R.id.healthDesc);

        HelperHealth h = healthItems.get(position);


        healthPic.setImageUrl(String.valueOf(h.getMem_pic_path()), imageLoader);
        healthTopic.setText(String.valueOf(h.getMem_topic()));
        //healthDesc.setText(String.valueOf(h.getMem_description()));

        return convertView;
    }


}