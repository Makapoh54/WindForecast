package com.test.anton.windforecast.windcast.favouritesdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.anton.windforecast.R;
import com.test.anton.windforecast.data.FavouritesRepository;
import com.test.anton.windforecast.data.database.FavouritesDBSource;
import com.test.anton.windforecast.data.remote.WeatherInjector;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;


public class FavouriteDetailsActivity extends AppCompatActivity implements FavouriteDetailsContract.View {

    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String CITY_NAME = "CITY_NAME";

    private Unbinder mUnbinder;
    private FavouriteDetailsPresenter mFavouritesListPresenter;

    private ValueLineSeries mSeries;

    @BindView(R.id.tv_details_time_period)
    TextView mTimePeriod;
    @BindView(R.id.tv_details_city)
    TextView mDetailsCity;
    @BindView(R.id.wind_chart)
    ValueLineChart mCubicValueLineChart;
    @BindView(R.id.progress_bar)
    View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_details);
        Timber.i("Favourites details acitivity created");
        mUnbinder = ButterKnife.bind(this);

        FavouritesRepository repository = FavouritesRepository.getInstance(FavouritesDBSource.getInstance(getApplicationContext()), WeatherInjector.provideWeatherService());

        String countryCode = getIntent().getStringExtra(COUNTRY_CODE);
        String cityName = getIntent().getStringExtra(CITY_NAME);
        mDetailsCity.setText(cityName);
        mFavouritesListPresenter = new FavouriteDetailsPresenter(this, repository, countryCode, cityName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavouritesListPresenter.loadForecast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFavouritesListPresenter.cancelRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void showDetails(@NotNull List<Float> forecast, @NotNull List<String> legend) {
        Timber.i("Forecast is shown");
        mSeries = new ValueLineSeries();
        mSeries.setColor(this.getResources().getColor(R.color.colorPrimary));
        for (int i = 0; i < forecast.size(); i++) {
            mSeries.addPoint(new ValueLinePoint(legend.get(i), forecast.get(i)));
            if (forecast.size() - i == 1) {
                String period = "Today - " + legend.get(i);
                mTimePeriod.setText(period);
            }
        }

        mCubicValueLineChart.clearChart();
        mCubicValueLineChart.addSeries(mSeries);
        mCubicValueLineChart.startAnimation();
    }

    @Override
    public void showLoadingError() {
        Toast.makeText(this, R.string.network_error_no_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
