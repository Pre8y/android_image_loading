package com.learncode.imagedownload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageManager {
    ThreadPoolExecutor downloadThreadPoolExecutor;
    ThreadPoolExecutor decodeThreadPoolExecutor;
    BlockingDeque<Runnable> downloadQueue;
    BlockingDeque<Runnable> decodeQueue;
    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;
    public static final int DOWNLOAD_COMPLETE = 0;
    public static final int DECODE_COMPLETE = 1;
    static ImageManager instance = null;

    private final LruCache<URL, byte[]> mPhotoCache;

    Handler mainHandler;
    public static ImageManager getInstance(){
        if(instance==null){
            instance = new ImageManager();
        }
        return instance;
    }


    private ImageManager() {
        downloadQueue = new LinkedBlockingDeque<>();
        decodeQueue = new LinkedBlockingDeque<>();
        this.downloadThreadPoolExecutor = new ThreadPoolExecutor(5,5,5,TimeUnit.SECONDS, downloadQueue);
        this.decodeThreadPoolExecutor = new ThreadPoolExecutor(5,5,5,TimeUnit.SECONDS, downloadQueue);
        mPhotoCache = new LruCache<URL, byte[]>(IMAGE_CACHE_SIZE) {

            /*
             * This overrides the default sizeOf() implementation to return the
             * correct size of each cache entry.
             */

            @Override
            protected int sizeOf(URL paramURL, byte[] paramArrayOfByte) {
                return paramArrayOfByte.length;
            }
        };
        mainHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                ImageTask task = (ImageTask) msg.obj;
                ImageView view = task.getViewRef().get();
                view.setImageBitmap(task.getImageBitmap());

            }
        };
    }

    public void loadImageUrl(String url, ImageView imageView){
        ImageTask imageTask = new ImageTask();
        imageTask.init(this, imageView, url);

        try {
            imageTask.setBuffer(mPhotoCache.get(new URL(url)));
            if(mPhotoCache.get(new URL(url))==null){
                downloadThreadPoolExecutor.execute(imageTask.getDownloadRunnable());
            }else{
                decodeThreadPoolExecutor.execute(imageTask.getDecodeRunnable());
            }

        }catch (Exception e){

        }
    }

    public void handle(ImageTask task,int state) {
        switch (state){
            case DOWNLOAD_COMPLETE:
                try {
                    mPhotoCache.put(new URL(task.getUrl()), task.getBuffer());
                    decodeThreadPoolExecutor.execute(task.getDecodeRunnable());
                }catch (Exception e){
                    Log.e("ERROR", e.toString());
                }
                break;
            case DECODE_COMPLETE:
                Message message  = mainHandler.obtainMessage(state, task);
                message.sendToTarget();
                break;


        }
    }
}
