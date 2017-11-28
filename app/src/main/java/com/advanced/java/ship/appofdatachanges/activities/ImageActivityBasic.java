package com.advanced.java.ship.appofdatachanges.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.downloaders.DataImageDownloaderTasks;

import java.util.concurrent.ExecutionException;

public class ImageActivityBasic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_basic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.image_view_test);
        Bitmap bitmap = null;
        try {
            bitmap = new DataImageDownloaderTasks(imageView, null).execute("images\\Хэллоуин\\PP.SL.003.000415s340x508.jpg").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(bitmap != null) {
            System.out.println("null");
            imageView.setImageBitmap(bitmap);
        }
        imageView.setBackgroundColor(0x00ff0000);

    }

}
