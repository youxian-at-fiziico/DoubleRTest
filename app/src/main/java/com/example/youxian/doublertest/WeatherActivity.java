package com.example.youxian.doublertest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by youxian on 12/8/15.
 */
public class WeatherActivity extends Activity implements Callback<WeatherResponse> {
    private static final String TAG = WeatherActivity.class.getName();

    private static final String WEB_SERVICE_BASE_URL = "http://api.openweathermap.org";
    private Call<WeatherResponse> mCall;

    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
    }

    private void initView() {
        Log.d(TAG, "init View");
        mStatus = (TextView) findViewById(R.id.status_text_weather);
        new LoadWeatherTask().execute();
    }

    @Override
    public void onResponse(Response<WeatherResponse> response, Retrofit retrofit) {
        Log.d(TAG, "onResponse");
        if (response.body() == null) {
            Log.d(TAG, "null");
        } else {
            Log.d(TAG, "not null");
            mStatus.setText(response.body().toString());
            Log.d(TAG, response.body().getWeather(0).getDescription());
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

            mCall = weatherService.fetchCurrentWeather();


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
