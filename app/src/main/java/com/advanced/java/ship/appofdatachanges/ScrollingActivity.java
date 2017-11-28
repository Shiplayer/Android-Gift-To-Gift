package com.advanced.java.ship.appofdatachanges;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyData data = new MyData();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("data"))
                data = intent.getParcelableExtra("data");
            if(intent.hasExtra("image")){
                byte[] bytes = intent.getByteArrayExtra("image");
                data.addBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }

        ImageView imageView = (ImageView) findViewById(R.id.toolbar_image_view);
        TextView textNameItem = (TextView) findViewById(R.id.scrolling_name_item);
        TextView textCostItem = (TextView) findViewById(R.id.scrolling_cost_item);
        TextView textCategoryItem = (TextView) findViewById(R.id.scrolling_category_item);
        TextView textDescription = (TextView) findViewById(R.id.scrolling_description_item);

        if(data.bitmapsLoaded.size() > 0) {
            imageView.setImageBitmap(data.bitmapsLoaded.get(0));
        }
        textNameItem.setText(data.name);
        textCostItem.setText(data.cost);
        textCategoryItem.setText(data.category);
        textDescription.setText(data.description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
