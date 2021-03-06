package com.example.youxian.doublertest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by youxian on 12/9/15.
 */
public class WeatherFragment extends Fragment implements Callback<WeatherResponse> {
    private static final String TAG = WeatherFragment.class.toString();
    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org";
    private Call<WeatherResponse> mCall;

    private TextView mUpdateText;
    private TextView mCityText;
    private ImageView mWeatherIcon;
    private TextView mDecriptionText;
    private TextView mHumidityText;
    private TextView mPressureText;
    private TextView mTemperatureText;
    private TextView mWeekdayText;
    private TextView mTemperatureMinText;
    private TextView mTemperatureMaxText;

    private ListView mListView;
    private ImageView mForecastIcon;
    private TextView mForecastDescription;

    private ForecastAdapter mForecastAdapter;
    private List<ForecastWeather.List> mForecastList;

    private String mCityString;
    private LoadWeatherTask mLoadWeatherTask;

    private WeatherResponse mWeatherResponse;
    private ForecastWeather mForecastWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((WeatherActivity)getActivity()).getCurrentWeather();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCityString = getArguments().getString(WeatherActivity.LOCATION_STRING);
        //Log.d(TAG, mCityString);
        //mLoadWeatherTask = new LoadWeatherTask();
        //mLoadWeatherTask.execute();
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUpdateText = (TextView) view.findViewById(R.id.update_text_weather);
        mCityText = (TextView) view.findViewById(R.id.city_text_weather);
        mWeatherIcon = (ImageView) view.findViewById(R.id.icon_image_weather);
        mDecriptionText = (TextView) view.findViewById(R.id.description_text_weather);
        mHumidityText = (TextView) view.findViewById(R.id.humidity_text_weather);
        mPressureText = (TextView) view.findViewById(R.id.pressure_text_weather);
        mTemperatureText = (TextView) view.findViewById(R.id.temperature_text_weather);
        mWeekdayText = (TextView) view.findViewById(R.id.weekday_text_weather);
        mTemperatureMaxText = (TextView) view.findViewById(R.id.maxTemp_text_weather);
        mTemperatureMinText = (TextView) view.findViewById(R.id.minTemp_text_weather);

        mListView = (ListView) view.findViewById(R.id.forecast_listview);
        mForecastIcon = (ImageView) view.findViewById(R.id.forecast_icon);
        mForecastDescription = (TextView) view.findViewById(R.id.forecast_description);
    }

    private void updateData() {
        String dateFormat = "dd/MM/yyyy hh:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        mUpdateText.setText(formatter.format(calendar.getTime()));
        //update image
        String description = mWeatherResponse.getWeather(0).getDescription();
        if (description.contains("clear")) {
            mWeatherIcon.setImageResource(R.drawable.clear);
        } else if (description.contains("rain")) {
            mWeatherIcon.setImageResource(R.drawable.rain);
        } else if (description.contains("cloud")) {
            mWeatherIcon.setImageResource(R.drawable.cloud);
        } else if (description.contains("snow")) {
            mWeatherIcon.setImageResource(R.drawable.snow);
        } else if (description.contains("mist")) {
            mWeatherIcon.setImageResource(R.drawable.mist);
        }

        mCityText.setText(mWeatherResponse.getName() + ", " + mWeatherResponse.getSys().getCountry());
        mDecriptionText.setText(mWeatherResponse.getWeather(0).getDescription());
        mHumidityText.setText("Humidity: " + mWeatherResponse.getMain().getHumidity() + " %");
        mPressureText.setText("Pressure: " + mWeatherResponse.getMain().getPressure() + " hPa");

        double temp = Double.parseDouble(mWeatherResponse.getMain().getTemp()) - 273.15;
        DecimalFormat tempFormat = new DecimalFormat("#.00");
        mTemperatureText.setText(tempFormat.format(temp) + " ℃");

        Calendar calendar1 = Calendar.getInstance();
        String dayLongName = calendar1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        mWeekdayText.setText(dayLongName);

        double tempMax = Double.parseDouble(mWeatherResponse.getMain().getTemp_max()) - 273.15;
        mTemperatureMaxText.setText(tempFormat.format(tempMax) + " ℃");

        double tempMin = Double.parseDouble(mWeatherResponse.getMain().getTemp_min()) - 273.15;
        mTemperatureMinText.setText(tempFormat.format(tempMin) + " ℃");

    }

    public void setWeatherResponse(WeatherResponse weatherResponse) {
        mWeatherResponse = weatherResponse;
        updateData();
    }

    private void updateForecast() {

    }

    public void setForecastWeather(ForecastWeather forecastWeather) {
        mForecastWeather = forecastWeather;
        updateForecast();
    }

    @Override
    public void onResponse(Response<WeatherResponse> response, Retrofit retrofit) {
        Log.d(TAG, "onResponse");
        if (response != null) {
            mWeatherResponse = response.body();
            updateData();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure");
    }

    private class ForecastAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mForecastList.size();
        }

        @Override
        public Object getItem(int position) {
            return mForecastList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    private static class ViewHolder {
        ImageView icon;
        TextView description;
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

            mCall = weatherService.fetchCurrentWeather(mCityString);


            mCall.enqueue(WeatherFragment.this);
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
