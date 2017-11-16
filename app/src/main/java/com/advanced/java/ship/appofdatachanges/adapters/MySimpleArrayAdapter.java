package com.advanced.java.ship.appofdatachanges.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.advanced.java.ship.appofdatachanges.downloaders.DataImageDownloaderTask;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anton on 30.10.2017.
 */

public class MySimpleArrayAdapter extends ArrayAdapter<MyData> {
    private final Context context;
    private final List<MyData> values;
    private final int resource;

    public MySimpleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyData> objects) {
        super(context, resource, objects);
        this.values = objects;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            System.out.println("create convertView");
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name_list);
            holder.cost = (TextView) convertView.findViewById(R.id.cost_list);
            holder.category = (TextView) convertView.findViewById(R.id.category_list);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view_list);
            /*Animation inAnim = new AlphaAnimation(0, 1);
            inAnim.setDuration(2000);
            Animation outAnim = new AlphaAnimation(1, 0);
            outAnim.setDuration(2000);
            holder.viewSwitcher.setInAnimation(inAnim);
            holder.viewSwitcher.setOutAnimation(outAnim);*/
            //holder.viewSwitcher.setAnimateFirstView(false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyData newsItem = values.get(position);
        System.out.println("newsItem = " + newsItem);
        holder.name.setText(newsItem.name);
        holder.cost.setText(newsItem.cost);
        holder.category.setText(newsItem.category);
        if(newsItem.pathImages.length > 0 && !newsItem.downloading){
            System.err.println("position = " + position);
            newsItem.downloading = true;
            new DataImageDownloaderTask(holder.imageView, newsItem).execute(newsItem.pathImages[0]);
            System.out.println("newsItem.bitmapsLoaded.size() = " + newsItem.bitmapsLoaded.size());
        } else if(newsItem.bitmapsLoaded.size() > 0) {
            //System.out.println("test");
            holder.imageView.setImageBitmap(newsItem.bitmapsLoaded.get(0));
            notifyDataSetChanged();
        } else {
            holder.imageView.setBackgroundColor(0x00ff00000);
        }

        if(position - 10 > values.size()){
            try {
                System.out.println("add new " + values.get(values.size() - 1).id);
                addAll(new DataDownloaderTask().execute("get " + values.get(values.size() - 1).id + " 100").get());
                notifyDataSetChanged();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //holder.viewSwitcher.setDisplayedChild(newsItem.state);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.viewSwitcher.showNext();
                if(values.get(position).state == 1)
                    values.get(position).state = 0;
                else
                    values.get(position).state = 1;
                //holder.viewSwitcher.showNext();
                //MySimpleArrayAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        public TextView name;
        public TextView cost;
        public TextView category;
        public ImageView imageView;
        public ViewSwitcher viewSwitcher;
    }
}
