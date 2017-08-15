package com.boostcamp.sentialarm.API.Weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 현기 on 2017-08-14.
 */

public class WeatherRootDTO {

    private List<WeatherDTO> weather;

    @SerializedName("main")
    private TemperateDTO temp;

    public List<WeatherDTO> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDTO> weather) {
        this.weather = weather;
    }

    public TemperateDTO getTemp() {
        return temp;
    }

    public void setTemp(TemperateDTO temp) {
        this.temp = temp;
    }
}
