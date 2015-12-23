package com.example.youxian.doublertest;

/**
 * Created by youxian on 12/23/15.
 */
public class ForecastWeather {

    private City city;
    private int cod;
    private String message;
    private int cnt;
    private List[] list;

    public City getCity() {
        return city;
    }

    public int getCnt() {
        return cnt;
    }

    public int getCod() {
        return cod;
    }

    public List[] getList() {
        return list;
    }

    public String getMessage() {
        return message;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void setList(List[] list) {
        this.list = list;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class City {
        private String id;
        private String name;
        private String country;

        public String getCountry() {
            return country;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public  class List {
        private String dt;
        private Temp temp;
        private Weather[] weather;

        public String getDt() {
            return dt;
        }

        public Temp getTemp() {
            return temp;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public void setWeather(Weather[] weather) {
            this.weather = weather;
        }
    }

    public class Temp {
        private String day;
        private String min;
        private String max;
        private String night;
        private String eve;
        private String morning;

        public String getDay() {
            return day;
        }

        public String getEve() {
            return eve;
        }

        public String getMax() {
            return max;
        }

        public String getMin() {
            return min;
        }

        public String getMorning() {
            return morning;
        }

        public String getNight() {
            return night;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public void setEve(String eve) {
            this.eve = eve;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public void setMorning(String morning) {
            this.morning = morning;
        }

        public void setNight(String night) {
            this.night = night;
        }
    }

    public class Weather {
        private String main;
        private String description;

        public String getDescription() {
            return description;
        }

        public String getMain() {
            return main;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setMain(String main) {
            this.main = main;
        }
    }


}
