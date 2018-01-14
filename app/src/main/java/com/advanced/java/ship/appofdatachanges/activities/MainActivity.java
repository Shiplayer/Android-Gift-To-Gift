package com.advanced.java.ship.appofdatachanges.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.advanced.java.ship.appofdatachanges.ActivityWithNavigation;
import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.ScrollingActivity;
import com.advanced.java.ship.appofdatachanges.adapters.MySimpleArrayAdapter;
import com.advanced.java.ship.appofdatachanges.downloaders.CategoryDownloaderTask;
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    List<MyData> myDataList;
    private static final String STATE_MY_DATA_LIST = "myDataList";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    // TODO медленно загружается, надо что-то сделать с этим
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_view);
        if(savedInstanceState == null) {
            myDataList = new ArrayList<>();
            /*try {
                //TODO как-то избавиться от get, т.к. он тормозит работу приложения
                myDataList = new DataDownloaderTask().execute("get 1 20").get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }*/
        } else {
            System.out.println("get myDataList from Instance");
            myDataList = savedInstanceState.getParcelableArrayList(STATE_MY_DATA_LIST);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.inflateHeaderView(R.layout.layout_navigation_main);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getResources().getString(R.string.drawer_open));
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);



        if(myDataList == null || myDataList.isEmpty()){
            myDataList = getFromResources();
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
        /*try {
            initializeAllInNavigation();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    private void initializeAllInNavigation() throws ExecutionException, InterruptedException {

        ListView NavigationListView = (ListView) findViewById(R.id.navigation_list);

        List<String> listOfString = Arrays.asList(this.getResources().getStringArray(R.array.catalog));//new CategoryDownloaderTask(this).execute("getCategory").get();
        Log.w("initializeAllIn", String.valueOf(listOfString.size()));
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfString);
        Log.w("check itemsAdapter", String.valueOf(NavigationListView == null));
        NavigationListView.setAdapter(itemsAdapter);

    }

    public List<MyData> getFromResources(){
        List<MyData> list = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray images = getResources().obtainTypedArray(R.array.images);
        @SuppressLint("Recycle") TypedArray id = getResources().obtainTypedArray(R.array.id);
        @SuppressLint("Recycle") TypedArray name = getResources().obtainTypedArray(R.array.name);
        @SuppressLint("Recycle") TypedArray description = getResources().obtainTypedArray(R.array.description);
        @SuppressLint("Recycle") TypedArray catalog = getResources().obtainTypedArray(R.array.catalog);
        @SuppressLint("Recycle") TypedArray cost = getResources().obtainTypedArray(R.array.cost);
        @SuppressLint("Recycle") TypedArray code = getResources().obtainTypedArray(R.array.code);
        if(images.length() == id.length()) {
            for (int i = 0; i < images.length(); i++){
                list.add(new MyData(id.getInt(i, -1), name.getString(i), cost.getString(i), description.getString(i), catalog.getString(i), images.getDrawable(i)));
            }
        } else  {
            Log.e("default data", "image.length() != data.length()");
        }
        return list;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
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
        Intent intent = new Intent(this, ActivityWithNavigation.class);
        startActivity(intent);
    }

    public void openScrollingActivity(View view){
        Intent intent = new Intent(this, ScrollingActivity.class);
        startActivity(intent);
    }

}
