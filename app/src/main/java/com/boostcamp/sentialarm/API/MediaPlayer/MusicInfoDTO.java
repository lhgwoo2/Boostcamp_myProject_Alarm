package com.boostcamp.sentialarm.API.MediaPlayer;

/**
 * Created by 현기 on 2017-08-06.
 */

public class MusicInfoDTO {

    private String name;
    private String artist_name;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }


}
