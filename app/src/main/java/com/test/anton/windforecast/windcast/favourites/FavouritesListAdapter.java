package com.test.anton.windforecast.windcast.favourites;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.anton.windforecast.R;
import com.test.anton.windforecast.models.ForecastedFavourite;
import com.test.anton.windforecast.utils.Utils;
import com.test.anton.windforecast.windcast.favouritesdetails.FavouriteDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> {

    private Context mContext;
    private List<ForecastedFavourite> mFavouriteList;
    private LayoutInflater mInflater;

    public FavouritesListAdapter(Context context, ArrayList<ForecastedFavourite> favouriteList) {
        mFavouriteList = favouriteList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public ForecastedFavourite getItem(int position) {
        return mFavouriteList.get(position);
    }

    public void setNewItems(List<ForecastedFavourite> newList) {
        mFavouriteList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.favourites_list_item, parent, false);

        return new ViewHolder(view, new FavouritesListAdapter.ViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mFavouriteList.get(position).getFavourite() != null) {
                    Intent intent = new Intent(mContext, FavouriteDetailsActivity.class);
                    intent.putExtra(FavouriteDetailsActivity.COUNTRY_CODE, mFavouriteList.get(position).getFavourite().getCountryCode());
                    intent.putExtra(FavouriteDetailsActivity.CITY_NAME, mFavouriteList.get(position).getFavourite().getCity());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ForecastedFavourite forecastedFavourite = mFavouriteList.get(position);

        if (forecastedFavourite.getFavourite() != null) {
            holder.mCity.setText(forecastedFavourite.getFavourite().getCity());
            holder.mCountry.setText(forecastedFavourite.getFavourite().getCountryCode());
        }
        if (forecastedFavourite.getWindForecast() != null) {
            String speed, direction;
            if (TextUtils.isEmpty(forecastedFavourite.getWindForecast().getWindSpeed())) {
                speed = mContext.getResources().getString(R.string.speed) +
                        mContext.getResources().getString(R.string.speed_meters_sec);
            } else {
                speed = forecastedFavourite.getWindForecast().getWindSpeed() +
                        mContext.getResources().getString(R.string.speed_meters_sec);
            }
            if (TextUtils.isEmpty(forecastedFavourite.getWindForecast().getWindDegree())) {
                direction = " (" + mContext.getResources().getString(R.string.direction) + ")";
            } else {
                direction = " (" + Utils.getWindCardinalDirection(forecastedFavourite.getWindForecast().getWindDegree()) + ")";
            }
            String windProp = speed + direction;
            holder.mWindProperties.setText(windProp);
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mFavouriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener mItemClickListener;
        @BindView(R.id.rl_favourites_list_item)
        RelativeLayout mItemLayout;
        @BindView(R.id.tv_favourite_city)
        TextView mCity;
        @BindView(R.id.tv_favourite_country)
        TextView mCountry;
        @BindView(R.id.tv_current_wind_properties)
        TextView mWindProperties;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            mItemClickListener = onItemClickListener;
            mItemLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
        }

        public static interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }
    }
}
