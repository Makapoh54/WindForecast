package com.test.anton.windforecast.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.data.database.FavouritesDBSource;
import com.test.anton.windforecast.models.ForecastedFavourite;
import com.test.anton.windforecast.models.WindForecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class FavouritesLoader extends AsyncTaskLoader<List<ForecastedFavourite>> implements FavouritesRepository.FavouritesRepositoryObserver {

    private FavouritesRepository mDataRepository;

    public FavouritesLoader(Context context, FavouritesRepository dataRep) {
        super(context);
        mDataRepository = dataRep;
        onContentChanged();
    }

    @Override
    public List<ForecastedFavourite> loadInBackground() {
        List<Favourite> favourites = mDataRepository.getFavourites();

        List<ForecastedFavourite> result = new ArrayList<>();
        for(Favourite favourite : favourites){
            result.add(new ForecastedFavourite(favourite, mDataRepository.getCurrentWindForecast(favourite.getCountryCode(), favourite.getCity())));
        }
        return result;
    }

    @Override
    public void deliverResult(List<ForecastedFavourite> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }

    }

    @Override
    protected void onStartLoading() {
        mDataRepository.addContentObserver(this);

        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mDataRepository.removeContentObserver(this);
    }

    @Override
    public void onFavouriteChanged() {
        onContentChanged();
    }
}
