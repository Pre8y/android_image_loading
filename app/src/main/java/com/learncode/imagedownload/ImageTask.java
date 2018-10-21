package com.learncode.imagedownload;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class ImageTask {
    ImageManager imageManager;

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    Bitmap imageBitmap;

    public WeakReference<ImageView> getViewRef() {
        return viewRef;
    }

    WeakReference<ImageView> viewRef; String url;
    int tagetWidth, targetHeight;

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    byte[]  buffer;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void init(ImageManager imageManager, ImageView imageView, String url) {
        this.imageManager = imageManager;
        this.viewRef = new WeakReference<ImageView>(imageView);
        this.url = url;
        this.tagetWidth = imageView.getWidth();
        this.targetHeight = imageView.getHeight();
    }

    public Runnable getDownloadRunnable() {
        return new DownloadRunnable(this);
    }

    public Runnable getDecodeRunnable() {
        return new DecodeRunnable(this);
    }

    public void handle(int state) {
        imageManager.handle(this,state);
    }
}
