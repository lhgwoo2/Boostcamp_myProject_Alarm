package com.boostcamp.sentialarm.API.Jamendo.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by 현기 on 2017-08-05.
 */

public class ResultDTO {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("releasedate")
    private Date releasedate;

    @SerializedName("artist_id")
    private long artist_id;

    @SerializedName("artist_name")
    private String artist_name;

    @SerializedName("image")
    private String image;

    @SerializedName("zip")
    private String zip;

    @SerializedName("tracks")
    private List<TrackDTO> tracks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
