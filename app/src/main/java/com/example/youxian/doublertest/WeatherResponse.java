package com.example.youxian.doublertest;

/**
 * Created by youxian on 12/8/15.
 */
public class WeatherResponse {
    private Weather[] weather;
    private String name;

    public Weather getWeather(int index) {
        if (index < weather.length - 1) {
            return weather[index];
        }
        return null;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
