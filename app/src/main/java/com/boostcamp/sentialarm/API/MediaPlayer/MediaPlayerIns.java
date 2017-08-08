package com.boostcamp.sentialarm.API.MediaPlayer;

import android.media.MediaPlayer;

/**
 * Created by 현기 on 2017-08-08.
 */

public class MediaPlayerIns {

    private static MediaPlayer mediaPlayer = null;

    private MediaPlayerIns() {
    }

    public static MediaPlayer getMusicPlayerIns() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            return mediaPlayer;
        } else {
            return mediaPlayer;
        }
    }
}
