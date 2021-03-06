package com.boostcamp.sentialarm.DTO;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 현기 on 2017-08-11.
 */

public class SongDTO extends RealmObject {

    @PrimaryKey
    private int id;

    private Date playDate;
    private String musicTitle;
    private String artistName;
    private String fileName;
    private String songShareURL;
    private WeatherInfoDTO weatherInfoDTO;
    private boolean localSong;

    public boolean isLocalSong() {
        return localSong;
    }

    public void setLocalSong(boolean localSong) {
        this.localSong = localSong;
    }

    public WeatherInfoDTO getWeatherInfoDTO() {
        return weatherInfoDTO;
    }

    public void setWeatherInfoDTO(WeatherInfoDTO weatherInfoDTO) {
        this.weatherInfoDTO = weatherInfoDTO;
    }

    public String getSongShareURL() {
        return songShareURL;
    }

    public void setSongShareURL(String songShareURL) {
        this.songShareURL = songShareURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPlayDate() {
        return playDate;
    }

    public void setPlayDate(Date playDate) {
        this.playDate = playDate;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
