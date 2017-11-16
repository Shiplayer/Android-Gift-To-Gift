package com.advanced.java.ship.appofdatachanges.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.adapters.MySimpleArrayAdapter;
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    List<MyData> myDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_view);
        myDataList = new ArrayList<>();
        try {
            myDataList = new DataDownloaderTask().execute("get 1 100").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        listView.setAdapter(new MySimpleArrayAdapter(this, R.layout.rowlayout, myDataList));
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MySimpleArrayAdapter.ViewHolder viewHolder = (MySimpleArrayAdapter.ViewHolder) view.getTag();
                viewHolder.category.setText(viewHolder.category.getText() + " " + viewHolder.name.getText());
                view.setTag(viewHolder);

            }
        });*/
        //Button btn = (Button) findViewById(R.id.button2);
    }

    public void openIntent(View view){
        System.out.println("test");
        Intent intent = new Intent(this, Main2ActivityGridView.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dataList", (ArrayList<? extends Parcelable>) myDataList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openActivityWithImage(View view){
        Intent intent = new Intent(this, ImageActivityBasic.class);
        startActivity(intent);
    }
}
