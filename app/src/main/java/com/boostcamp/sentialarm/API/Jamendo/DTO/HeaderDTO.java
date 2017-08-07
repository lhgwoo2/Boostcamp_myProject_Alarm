package com.boostcamp.sentialarm.API.Jamendo.DTO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 현기 on 2017-08-05.
 */

public class HeaderDTO {

    @SerializedName("status")
    private String status;

    @SerializedName("code")
    private long code;

    @SerializedName("error_message")
    private String error_message;

    @SerializedName("warnings")
    private String warnings;

    @SerializedName("results_count")
    private long results_count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public long getResults_count() {
        return results_count;
    }

    public void setResults_count(long results_count) {
        this.results_count = results_count;
    }
}
