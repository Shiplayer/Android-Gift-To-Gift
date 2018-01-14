package com.advanced.java.ship.appofdatachanges.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Anton on 28.11.2017.
 */

public class BaseElementsAdapter extends ArrayAdapter<MyData> {
    protected final Context context;
    protected final List<MyData> values;
    protected final int resource;

    public BaseElementsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyData> objects) {
        super(context, resource, objects);
        this.values = objects;
        this.context = context;
        this.resource = resource;
    }
}
