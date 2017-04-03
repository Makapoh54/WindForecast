package com.test.anton.windforecast;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.test.anton.windforecast.data.FavouritesLoader;
import com.test.anton.windforecast.data.FavouritesRepository;
import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.models.ForecastedFavourite;
import com.test.anton.windforecast.models.WindForecast;
import com.test.anton.windforecast.windcast.favourites.FavouritesContract;
import com.test.anton.windforecast.windcast.favourites.FavouritesListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class FavouritesListPresenterTest {

    private static List<ForecastedFavourite> mFavourites = new ArrayList<>();

    @Mock
    private FavouritesRepository mFavouritesRepository;

    @Mock
    private FavouritesContract.View mFavouritesView;

    @Captor
    private ArgumentCaptor<List> mShowFavouritesArgumentCaptor;

    @Mock
    private FavouritesLoader mFavouritesLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private FavouritesListPresenter mFavouritesListPresenter;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);

        mFavouritesListPresenter = new FavouritesListPresenter(
                mFavouritesLoader, mLoaderManager, mFavouritesView, mFavouritesRepository);

        mFavourites.add(new ForecastedFavourite(new Favourite("LV", "Riga"), new WindForecast("2.0", "200")));
        mFavourites.add(new ForecastedFavourite(new Favourite("UK", "London"), new WindForecast("7.0", "250")));
    }

    @Test
    public void loadFavouritesAndShow() {
        mFavouritesListPresenter.onLoadFinished(mock(Loader.class), mFavourites);

        verify(mFavouritesView).setLoadingIndicator(false);
        verify(mFavouritesView).showFavourites(mShowFavouritesArgumentCaptor.capture());
        assertThat(mShowFavouritesArgumentCaptor.getValue().size(), is(2));
    }

    @Test
    public void showAlreadyAddedTest() {
        mFavouritesListPresenter.onLoadFinished(mock(Loader.class), mFavourites);
        mFavouritesListPresenter.saveFavourite(new Favourite("LV", "Riga"));

        verify(mFavouritesView).showAlreadyAdded();
    }

    @Test
    public void showLoadingErrorTest() {
        mFavouritesListPresenter.onLoadFinished(mock(Loader.class), null);

        verify(mFavouritesView).showLoadingError();
    }
}
