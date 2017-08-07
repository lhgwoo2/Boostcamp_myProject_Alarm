package com.boostcamp.sentialarm.API.Jamendo.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by 현기 on 2017-08-05.
 */

public class TrackDTO {

    @SerializedName("id")
    private long id;

    @SerializedName("position")
    private long position;

    @SerializedName("name")
    private String name;

    @SerializedName("duration")
    private long duration;

    @SerializedName("license_ccurl")
    private String license_ccurl;

    @SerializedName("audio")
    private String audio;

    @SerializedName("audiodownload")
    private String audiodownload;

    @SerializedName("artist_id")
    private long artist_id;

    @SerializedName("artist_name")
    private String artist_name;

    @SerializedName("artist_idstr")
    private String artist_idstr;

    @SerializedName("album_name")
    private String album_name;

    @SerializedName("album_id")
    private String album_id;

    @SerializedName("releasedate")
    private Date releasedate;

    @SerializedName("album_image")
    private String album_image;

    @SerializedName("prourl")
    private String prourl;

    @SerializedName("shorturl")
    private String shorturl;

    @SerializedName("shareurl")
    private String shareurl;

    @SerializedName("image")
    private String image;

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

    public String getArtist_idstr() {
        return artist_idstr;
    }

    public void setArtist_idstr(String artist_idstr) {
        this.artist_idstr = artist_idstr;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getProurl() {
        return prourl;
    }

    public void setProurl(String prourl) {
        this.prourl = prourl;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLicense_ccurl() {
        return license_ccurl;
    }

    public void setLicense_ccurl(String license_ccurl) {
        this.license_ccurl = license_ccurl;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudiodownload() {
        return audiodownload;
    }

    public void setAudiodownload(String audiodownload) {
        this.audiodownload = audiodownload;
    }
}
