package com.example.youxian.doublertest;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by youxian on 12/8/15.
 */
public interface WeatherService  {
    @GET("/data/2.5/weather?appid=2de143494c0b295cca9337e1e96b00e0")
    Call<WeatherResponse> fetchCurrentWeather(@Query("q") String city);
}
