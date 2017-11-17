package com.advanced.java.ship.appofdatachanges.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
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
    private static final String STATE_MY_DATA_LIST = "myDataList";

    // TODO медленно загружается, надо что-то сделать с этим
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_view);
        if(savedInstanceState == null) {
            myDataList = new ArrayList<>();
            try {
                myDataList = new DataDownloaderTask().execute("get 1 100").get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("get myDataList from Instance");
            myDataList = savedInstanceState.getParcelableArrayList(STATE_MY_DATA_LIST);
        }

        assert myDataList != null;
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

    @Override
    protected void onPause() {
        System.err.println("pause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        System.err.println("stop");
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        System.out.println("load myDataList in Instance");
        outState.putParcelableArrayList(STATE_MY_DATA_LIST, (ArrayList<? extends Parcelable>) myDataList);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    // TODO научиться кэшировать Bitmap
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
