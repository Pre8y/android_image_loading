package com.learncode.imagedownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageManager manager = ImageManager.getInstance();
        String url1 = "https://tineye.com/images/widgets/mona.jpg";
        ImageView imageView1 = findViewById(R.id.image1);
        manager.loadImageUrl(url1, imageView1);
        String url2 = "https://www.gstatic.com/webp/gallery/2.jpg";
        ImageView imageView2 = findViewById(R.id.image2);
        manager.loadImageUrl(url2, imageView2);
        String url3 = "https://www.gstatic.com/webp/gallery/1.jpg";
        ImageView imageView3 = findViewById(R.id.image3);
        manager.loadImageUrl(url3, imageView3);
    }
}
