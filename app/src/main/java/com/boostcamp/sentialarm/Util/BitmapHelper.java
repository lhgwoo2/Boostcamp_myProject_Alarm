package com.boostcamp.sentialarm.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by 현기 on 2017-08-12.
 */

public class BitmapHelper {

    public Bitmap getBitmapOnURL(String url) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap mBitmap = null;
        try {
            mBitmap = BitmapFactory
                    .decodeStream((InputStream) new URL(url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBitmap;
    }

    public String bitmapSaveInApp(Context context, Bitmap bitmap, MusicDTO musicDTO) {

        String fileName = makeFileNameToJPG(musicDTO);

        FileOutputStream fos = null;
        try {

            fos = context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);       // 품질 80%로 변경
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }

        return fileName;
    }

    // 가수이름과 노래제목으로 파일 이름 생성, 중복된 파일이 존재할 경우 replace하기 위해
    private String makeFileNameToJPG(MusicDTO musicDTO) {

        String fileName = musicDTO.getResults().get(0).getArtist_name() + "_" + musicDTO.getResults().get(0).getName() + ".jpg";

        Log.i("tests", "파일이름 : " + fileName);
        return fileName;
    }

    // 해당 파일의 앱 내부 파일 Path를 가져온다.
    public String getImageResourcePath(String fileName, Context context) {

        File getFile = context.getApplicationContext().getFileStreamPath(fileName);
        String filePath = getFile.getAbsolutePath();
        Log.i("filePath", filePath);

        return filePath;
    }
}
