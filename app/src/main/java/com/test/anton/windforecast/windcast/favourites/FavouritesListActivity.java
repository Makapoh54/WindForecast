package com.test.anton.windforecast.windcast.favourites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.anton.windforecast.R;
import com.test.anton.windforecast.data.FavouritesLoader;
import com.test.anton.windforecast.data.FavouritesRepository;
import com.test.anton.windforecast.data.database.FavouritesDBSource;
import com.test.anton.windforecast.data.remote.WeatherInjector;
import com.test.anton.windforecast.dialogpicker.LocationDialogPicker;
import com.test.anton.windforecast.dialogpicker.LocationPickerListener;
import com.test.anton.windforecast.models.Favourite;
import com.test.anton.windforecast.models.ForecastedFavourite;
import com.test.anton.windforecast.network.NetworkManager;
import com.test.anton.windforecast.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;


public class FavouritesListActivity extends AppCompatActivity implements FavouritesContract.View {

    private FavouritesListAdapter mFavouritesListAdapter;
    private FavouritesListPresenter mFavouritesListPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.favourites_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_list_no_items)
    TextView mNoItems;
    @BindView(R.id.progress_bar)
    View mProgressBar;

    @OnClick(R.id.fl_bttn)
    public void floating_button() {
        final LocationDialogPicker countryPicker = LocationDialogPicker.newInstance("Select Country", true, null);
        countryPicker.show(getSupportFragmentManager(), "Country Picker");
        countryPicker.setListener(new LocationPickerListener() {
            @Override
            public void onSelectItem(final String country) {

                final LocationDialogPicker cityPicker = LocationDialogPicker.newInstance("Select City", false, country);
                cityPicker.show(getSupportFragmentManager(), "City Picker");
                cityPicker.setListener(new LocationPickerListener() {
                    @Override
                    public void onSelectItem(String city) {
                        mFavouritesListPresenter.saveFavourite(new Favourite(country, city));
                        cityPicker.dismiss();
                        countryPicker.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);
        Timber.i("FavouritesListActivity created");
        mUnbinder = ButterKnife.bind(this);

        mFavouritesListAdapter = new FavouritesListAdapter(this, new ArrayList<ForecastedFavourite>(0));
        FavouritesRepository repository = FavouritesRepository.getInstance(FavouritesDBSource.getInstance(getApplicationContext()), WeatherInjector.provideWeatherService());
        FavouritesLoader favouritesLoader = new FavouritesLoader(getApplicationContext(), repository);

        mFavouritesListPresenter = new FavouritesListPresenter(
                favouritesLoader,
                getSupportLoaderManager(),
                this,
                repository
        );

        configureLayout();
    }

    private void configureLayout() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFavouritesListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayout.VERTICAL));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkManager.getInstance().isConnected()) {
                    mFavouritesListPresenter.loadFavourites();
                } else {
                    Toast.makeText(mSwipeRefreshLayout.getContext(), R.string.network_error_no_connection, Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavouritesListPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFavourites(@NonNull List<ForecastedFavourite> favourites) {
        Timber.i("Favourites list is shown");
        if (favourites.size() == 0) {
            Utils.showView(mNoItems);
        } else {
            Utils.hideView(mNoItems);
        }
        mFavouritesListAdapter.setNewItems(favourites);
    }

    @Override
    public void showAlreadyAdded() {
        Toast.makeText(this, R.string.already_added_message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showLoadingError() {
        Toast.makeText(this, R.string.network_error_no_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showProgress();
        } else {
            hideProgress();
        }
    }
}
