package com.advanced.java.ship.appofdatachanges.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.ScrollingActivity;
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
                myDataList = new DataDownloaderTask().execute("get 1 20").get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("get myDataList from Instance");
            myDataList = savedInstanceState.getParcelableArrayList(STATE_MY_DATA_LIST);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setCustomView(R.layout.actionbar_view);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            //actionBar.setDisplayShowHomeEnabled(true);
            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        } else {
            Log.e("actionBar", "is null!!");
        }

        assert myDataList != null;
        listView.setAdapter(new MySimpleArrayAdapter(this, R.layout.rowlayout, myDataList));
        listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("ScrollView","scrollX_"+scrollX+"_scrollY_"+scrollY+"_oldScrollX_"+oldScrollX+"_oldScrollY_"+oldScrollY);
            }
        });
        listView.getScrollIndicators();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Log.d("onScrollStateChanged","i"+i);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Log.d("onScroll","i " + i + " i1 " + i1 + " i2 " + i2);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.w("warning", item.getItemId() + " vs " + android.R.id.home);
        return super.onOptionsItemSelected(item);
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

    public void openScrollingActivity(View view){
        Intent intent = new Intent(this, ScrollingActivity.class);
        startActivity(intent);
    }

}
