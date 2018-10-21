package com.learncode.imagedownload;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadRunnable implements Runnable {
    ImageTask task;
    public DownloadRunnable(ImageTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            URL url = new URL(task.getUrl());
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            byte[] data = new byte[16384];
            int nRead = -1;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            task.setBuffer( buffer.toByteArray());
            task.handle(ImageManager.DOWNLOAD_COMPLETE);

        }catch (Exception e){
            Log.e("ERROR", e.toString());
        }
    }
}
