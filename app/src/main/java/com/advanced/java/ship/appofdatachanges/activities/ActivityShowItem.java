package com.advanced.java.ship.appofdatachanges.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

public class ActivityShowItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        MyData data = new MyData();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("data"))
                data = intent.getParcelableExtra("data");
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageView_item);
        TextView textNameItem = (TextView) findViewById(R.id.text_name_item);
        TextView textCostItem = (TextView) findViewById(R.id.text_cost_item);
        TextView textCategoryItem = (TextView) findViewById(R.id.text_category_item);

        textNameItem.setText(data.name);
        textCostItem.setText(data.cost);
        textCategoryItem.setText(data.category);


    }
}
