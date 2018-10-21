package com.learncode.imagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecodeRunnable implements Runnable {
    ImageTask task;
    public DecodeRunnable(ImageTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        byte[] bitmapdata = task.getBuffer();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        task.setImageBitmap(bitmap);
        task.handle(ImageManager.DECODE_COMPLETE);
    }
}
