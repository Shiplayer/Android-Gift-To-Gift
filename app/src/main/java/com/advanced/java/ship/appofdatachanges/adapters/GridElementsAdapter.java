package com.advanced.java.ship.appofdatachanges.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anton on 01.11.2017.
 */

public class GridElementsAdapter extends ArrayAdapter<MyData>{
    private Context context;
    private List<MyData> arrData;
    private int layoutResource;


    public GridElementsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyData> objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        this.arrData = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public MyData getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println(position);
        View row = convertView;
        final ViewHolder viewHolder;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.text_name);
            viewHolder.cost = (TextView) row.findViewById(R.id.text_price);
            viewHolder.category = (TextView) row.findViewById(R.id.text_category);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.imageView3);
            viewHolder.viewSwitcher = (ViewSwitcher) row.findViewById(R.id.swithcher);
            viewHolder.viewSwitcher.setAnimateFirstView(false);
            Animation inAnim = new AlphaAnimation(0, 1);
            inAnim.setDuration(1000);
            Animation outAnim = new AlphaAnimation(1, 0);
            outAnim.setDuration(1000);

            viewHolder.viewSwitcher.setInAnimation(inAnim);
            viewHolder.viewSwitcher.setOutAnimation(outAnim);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        MyData MyData = arrData.get(position);
        viewHolder.name.setText(MyData.name);
        viewHolder.cost.setText(MyData.cost);
        viewHolder.category.setText(MyData.category);
        viewHolder.imageView.setBackgroundColor(0x00ff00ff);
        viewHolder.viewSwitcher.setDisplayedChild(MyData.state);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.viewSwitcher.showNext();
                if(arrData.get(position).state == 1)
                    arrData.get(position).state = 0;
                else
                    arrData.get(position).state = 1;
                viewHolder.viewSwitcher.showNext();
            }
        });

        /*
        holder.viewSwitcher.setDisplayedChild(newsItem.state);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.viewSwitcher.showNext();
                if(values[position].state == 1)
                    values[position].state = 0;
                else
                    values[position].state = 1;
                holder.viewSwitcher.showNext();
                //MySimpleArrayAdapter.this.notifyDataSetChanged();
            }
        });
         */

        return row;
    }

    public static class ViewHolder{
        public TextView name;
        public TextView cost;
        public TextView category;
        public ImageView imageView;
        public ViewSwitcher viewSwitcher;
    }
}
