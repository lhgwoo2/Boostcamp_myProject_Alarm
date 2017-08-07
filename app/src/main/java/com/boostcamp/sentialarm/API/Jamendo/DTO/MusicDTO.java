package com.boostcamp.sentialarm.API.Jamendo.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 현기 on 2017-08-06.
 */

public class MusicDTO {
    @SerializedName("headers")
    private HeaderDTO headers;

    @SerializedName("results")
    private List<TrackDTO> results;

    public HeaderDTO getHeaders() {
        return headers;
    }

    public void setHeaders(HeaderDTO headers) {
        this.headers = headers;
    }

    public List<TrackDTO> getResults() {
        return results;
    }

    public void setResults(List<TrackDTO> results) {
        this.results = results;
    }
}
