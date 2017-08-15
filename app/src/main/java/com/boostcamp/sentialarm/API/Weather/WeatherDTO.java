package com.boostcamp.sentialarm.API.Weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 현기 on 2017-08-14.
 */

public class WeatherDTO {

    private int id;
    @SerializedName("main")
    private String mainCondition;
    private String desciption;
    private String icon;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainCondition() {
        return mainCondition;
    }

    public void setMainCondition(String mainCondition) {
        this.mainCondition = mainCondition;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
