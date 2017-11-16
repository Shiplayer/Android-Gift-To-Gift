package com.advanced.java.ship.appofdatachanges.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ViewSwitcher;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.adapters.GridElementsAdapter;
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main2ActivityGridView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_grid_view);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        /*List<MyData> myDataList = new ArrayList<>();
        try {
            myDataList = new DataDownloaderTask().execute("get 1 100").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }*/
        ArrayList<MyData> myDataList = getIntent().getExtras().getParcelableArrayList("dataList");
        if(myDataList == null){
            System.err.println("error. list is empty");
            finish();
        }
        gridView.setAdapter(new GridElementsAdapter(this, R.layout.gridelements, myDataList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GridElementsAdapter.ViewHolder holder = (GridElementsAdapter.ViewHolder) adapterView.getSelectedItem();
                System.out.println(holder.name.getText());
                holder.viewSwitcher.showNext();
            }
        });
    }
}
