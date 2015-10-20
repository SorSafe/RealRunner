package com.example.acerth.helper;

import com.example.acerth.app.AppController;
import com.example.acerth.helper.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.acerth.realrunner.R;

public class CustomListAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private List<User> originalData = null;
    private List<User> filteredData = null;
    private LayoutInflater mInflater;
    private UserFilter mFilter = new UserFilter();
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<User> data) {
        this.filteredData = data ;
        this.originalData = data ;
        mInflater = LayoutInflater.from(activity);
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null){
            mFilter  = new UserFilter();
        }
        return mFilter;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int location) {
        return filteredData.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null)
            mInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.list_user_row, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView userID = (TextView)convertView.findViewById(R.id.user_id);
        TextView userName = (TextView)convertView.findViewById(R.id.user_name);

        User u = filteredData.get(position);

        thumbNail.setImageUrl(String.valueOf(u.getThumbnailUrl()), imageLoader);
        userID.setText(String.valueOf(u.getUser_id()));
        userName.setText(String.valueOf(u.getUser_game_name()));

        return convertView;
    }



    private class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<User> list = originalData;

            int count = list.size();
            final ArrayList<User> nlist = new ArrayList<User>(count);

            User filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toString().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }


}

