package com.boostcamp.sentialarm.DTO;

import io.realm.RealmObject;

/**
 * Created by 현기 on 2017-08-16.
 */

public class WeatherInfoDTO extends RealmObject{

    private String weatherFileName;
    private String address;
    private int weatherIconID;
    private String temperate;

    public String getWeatherFileName() {
        return weatherFileName;
    }

    public void setWeatherFileName(String weatherFileName) {
        this.weatherFileName = weatherFileName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWeatherIconID() {
        return weatherIconID;
    }

    public void setWeatherIconID(int weatherIconID) {
        this.weatherIconID = weatherIconID;
    }

    public String getTemperate() {
        return temperate;
    }

    public void setTemperate(String temperate) {
        this.temperate = temperate;
    }
}
