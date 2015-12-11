package com.example.youxian.doublertest;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by youxian on 12/8/15.
 */
public class WeatherActivity extends AppCompatActivity implements Callback<WeatherResponse> {
    private static final String TAG = WeatherActivity.class.getName();
    private static final String TAG_FRAGMENT = "tag_fragment";
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org";

    public static final String LOCATION_STRING = "location_string";

    private Call<WeatherResponse> mCall;

    private WeatherFragment mWeatherFragment;
    private LocationManager mLocationManager;
    private String mLocationString;
    private String mCityString;

    private SearchView mSearchView;
    private WeatherResponse mWeatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initView();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.search_city));
        // Configure the search info and add any event listeners...
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "query submit");
                mSearchView.clearFocus();
                searchWeather(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "query change");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.d(TAG, "search selected");
                return true;

            case R.id.action_settings:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void searchWeather(String cityname) {
        Log.d(TAG, cityname);
        new LoadWeatherTask().execute(cityname);
    }

    private void initView() {
        Log.d(TAG, "init View");
        replaceFragment(getWeatherFragment(), false);


        /*
        DecimalFormat locationFormat = new DecimalFormat("#.00");
        mLocationString = locationFormat.format(location.getLatitude())
                + "@" + locationFormat.format(location.getLongitude());
        */
    }

    public void getCurrentWeather() {
        Location location = getLastKnownLocation();
        if (location != null) {
            Log.d(TAG, "location not null");
            Geocoder gcd = new Geocoder(this, Locale.ENGLISH);
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String separatedString[];
            if (addresses.size() > 0){
                Log.d(TAG, addresses.get(0).getAdminArea());
                separatedString = addresses.get(0).getAdminArea().split(" ");
                mCityString = separatedString[0];
            }
            new LoadWeatherTask().execute();
        }
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_weather, fragment, TAG_FRAGMENT);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private WeatherFragment getWeatherFragment() {
        if (mWeatherFragment == null) {
            mWeatherFragment = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString(LOCATION_STRING, mCityString);
            mWeatherFragment.setArguments(bundle);
        }
        return mWeatherFragment;
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    @Override
    public void onResponse(Response<WeatherResponse> response, Retrofit retrofit) {
        Log.d(TAG, "onResponse");
        if (response.body() == null) {
            Log.d(TAG, "null");
        } else {
            Log.d(TAG, "not null");
            mWeatherResponse = response.body();
            //Log.d(TAG, mWeatherResponse.getMain().getPressure());
            //Log.d(TAG, mWeatherResponse.getWeather(0).getMain());
            mWeatherFragment.setWeatherResponse(mWeatherResponse);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure");
        Log.d(TAG, t.toString());
    }

    private class LoadWeatherTask extends AsyncTask<String , Void, Void> {

        @Override
        protected Void doInBackground(String ... params) {
            Retrofit mRetrofit = new Retrofit.Builder()
                    .baseUrl(WEB_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // prepare call in Retrofit 2.0
            WeatherService weatherService = mRetrofit.create(WeatherService.class);
            if (params.length > 0) {
                Log.d(TAG, "with params");
                mCall = weatherService.fetchCurrentWeather(params[0]);
            } else {
                Log.d(TAG, "no params");
                mCall = weatherService.fetchCurrentWeather(mCityString);
            }



            mCall.enqueue(WeatherActivity.this);
            // synchronous call would be with execute, in this case you
            // would have to perform this outside the main thread
            // call.execute()

            // to cancel a running request
            // call.cancel();
            // calls can only be used once but you can easily clone them
            //Call<StackOverflowQuestions> c = call.clone();
            //c.enqueue(this);
            return null;
        }
    }

}
