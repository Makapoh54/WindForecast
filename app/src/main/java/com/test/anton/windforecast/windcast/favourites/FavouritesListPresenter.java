package com.test.anton.windforecast.windcast.favourites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.test.anton.windforecast.data.FavouritesLoader;
import com.test.anton.windforecast.data.FavouritesRepository;
import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.models.ForecastedFavourite;

import java.util.List;

import timber.log.Timber;


public class FavouritesListPresenter implements FavouritesContract.Presenter, LoaderManager.LoaderCallbacks<List<ForecastedFavourite>> {

    private final static int TASKS_QUERY = 1;

    private final FavouritesContract.View mFavouritesView;

    private List<ForecastedFavourite> mFavourites;

    private final FavouritesRepository mFavouritesRepository;
    private final FavouritesLoader mLoader;
    private final LoaderManager mLoaderManager;


    public FavouritesListPresenter(@NonNull FavouritesLoader loader, @NonNull LoaderManager loaderManager,
                                   @NonNull FavouritesContract.View favouritesView, @NonNull FavouritesRepository dataSource) {
        Timber.i("FavouritesListPresenter created");
        mLoader = loader;
        mLoaderManager = loaderManager;
        mFavouritesView = favouritesView;
        mFavouritesRepository = dataSource;
    }

    @Override
    public Loader<List<ForecastedFavourite>> onCreateLoader(int id, Bundle args) {
        mFavouritesView.setLoadingIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ForecastedFavourite>> loader, List<ForecastedFavourite> data) {
        mFavouritesView.setLoadingIndicator(false);

        mFavourites = data;
        if (mFavourites == null) {
            mFavouritesView.showLoadingError();
        } else {
            mFavouritesView.showFavourites(data);
        }
        mFavouritesView.onItemsLoadComplete();
    }

    @Override
    public void onLoaderReset(Loader<List<ForecastedFavourite>> loader) {
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(TASKS_QUERY, null, this);
    }

    @Override
    public void loadFavourites() {
        mFavouritesRepository.refreshFavourites();
    }

    @Override
    public void saveFavourite(@NonNull Favourite favourite) {
        if (!isFavourite(favourite)) {
            mFavouritesRepository.saveFavourite(favourite);
        } else {
            mFavouritesView.showAlreadyAdded();
        }
    }

    private boolean isFavourite(@NonNull Favourite favourite) {
        for (ForecastedFavourite forecastedFavourite : mFavourites) {
            if (forecastedFavourite.getFavourite().getCity().equals(favourite.getCity()) &&
                    forecastedFavourite.getFavourite().getCountryCode().equals(favourite.getCountryCode())) {
                return true;
            }
        }
        return false;
    }
}