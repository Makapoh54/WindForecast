package com.test.anton.windforecast.dialogpicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.test.anton.windforecast.R;
import com.test.anton.windforecast.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LocationDialogPicker extends DialogFragment {

    private LocationListAdapter mAdapter;
    private LocationPickerListener mListener;
    private boolean isCountry;
    private String mCountryCode;
    private List<String> mLocationList = new ArrayList<>();
    private List<String> mSearchedLocationList = new ArrayList<>();

    @BindView(R.id.et_location_search)
    EditText mSearchEditText;
    @BindView(R.id.location_search_list)
    ListView mListView;

    public static LocationDialogPicker newInstance(String dialogTitle, boolean isCountry, String countryCode) {
        LocationDialogPicker picker = new LocationDialogPicker();
        picker.setCountryCode(countryCode);
        picker.setCountry(isCountry);
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_location_picker, null);
        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if (args != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }

        if (isCountry && mCountryCode == null) {
            mSearchEditText.setHint(R.string.search_country);
            mLocationList = getAllCountries();
        } else {
            mSearchEditText.setHint(R.string.search_city);
            mLocationList = getCitiesForCountry(mCountryCode);
        }
        mSearchedLocationList = new ArrayList<>(mLocationList);

        mAdapter = new LocationListAdapter(getActivity(), mLocationList, isCountry);
        mListView.setAdapter(mAdapter);

        initListeners();
        return view;
    }

    private void initListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    String value;
                    if (isCountry) {
                        value = Utils.getCountryCode(mSearchedLocationList.get(position));
                    } else {
                        value = mSearchedLocationList.get(position);
                    }
                    mListener.onSelectItem(value);
                }
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

    }


    public void setListener(LocationPickerListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        mSearchedLocationList.clear();
        for (String location : mLocationList) {
            if (location.toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                mSearchedLocationList.add(location);
            }
        }
        mAdapter.setNewItems(mSearchedLocationList);
    }

    public EditText getSearchEditText() {
        return mSearchEditText;
    }

    public ListView getListView() {
        return mListView;
    }

    public List<String> getAllCountries() {
        List<String> countriesList = new ArrayList<String>();
        try {
            InputStream is = getContext().getAssets().open("current_country.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            String json = new String(buffer, "UTF-8");

            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                countriesList.add(arr.getString(i));
            }

            return countriesList;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<String> getCitiesForCountry(String countryCode) {
        List<String> citiesList = new ArrayList<String>();
        try {
            InputStream is = getContext().getAssets().open("current_cities.json");

            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            JsonParser jsonParser = new JsonParser();
            JsonObject result = null;

            reader.beginObject();
            while (reader.hasNext()) {

                String name = reader.nextName();

                if (name.equals(countryCode)) {

                    reader.beginArray();

                    while (reader.hasNext()) {
                        citiesList.add(reader.nextString());
                    }

                    reader.endArray();
                    break;
                } else {
                    reader.skipValue();
                }
            }

            reader.close();
            return citiesList;

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return citiesList;
        } catch (IOException ex) {
            ex.printStackTrace();
            return citiesList;
        }
    }

    public boolean isCountry() {
        return isCountry;
    }

    public void setCountry(boolean country) {
        isCountry = country;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }
}
