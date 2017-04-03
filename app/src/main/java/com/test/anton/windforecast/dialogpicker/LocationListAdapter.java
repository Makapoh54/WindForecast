package com.test.anton.windforecast.dialogpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.test.anton.windforecast.R;
import com.test.anton.windforecast.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mLocations;
    private LayoutInflater mInflater;
    private boolean mPickCountry;

    public LocationListAdapter(Context context, List<String> locations, boolean isCountry) {
        mContext = context;
        mLocations = locations;
        mInflater = LayoutInflater.from(context);
        mPickCountry = isCountry;
    }

    public void setNewItems(List<String> newList) {
        mLocations = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLocations.size();
    }

    @Override
    public Object getItem(int i) {
        return mLocations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String location = mLocations.get(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.location_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mPickCountry) {
            holder.mTitle.setText(location + "(" + Utils.getCountryCode(location) + ")");
        } else {
            holder.mTitle.setText(location);
        }

        convertView.setTag(holder);
        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.tv_location_name)
        TextView mTitle;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}