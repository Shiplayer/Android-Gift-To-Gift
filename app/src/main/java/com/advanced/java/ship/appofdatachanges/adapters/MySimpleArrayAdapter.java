package com.advanced.java.ship.appofdatachanges.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.advanced.java.ship.appofdatachanges.ScrollingActivity;
import com.advanced.java.ship.appofdatachanges.activities.ActivityShowItem;
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.downloaders.DataImageDownloaderTasks;
import com.advanced.java.ship.appofdatachanges.downloaders.ThreadPoolDownloadImage;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Anton on 30.10.2017.
 */

public class MySimpleArrayAdapter extends ArrayAdapter<MyData> {
    private final Context context;
    private final List<MyData> values;
    private final int resource;
    private final ExecutorService threadPoolExecutor;

    public MySimpleArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyData> objects) {
        super(context, resource, objects);
        this.values = objects;
        this.context = context;
        this.resource = resource;
        threadPoolExecutor = Executors.newFixedThreadPool(10);
    }

    // TODO сделать переход к товару по одному клику в списке

    // TODO почему-то при скачивании картинки, она постоянно меняется только на первом элементе
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
        //System.out.println("newsItem = " + newsItem);
        holder.name.setText(newsItem.name);
        holder.cost.setText(newsItem.cost);
        holder.category.setText(newsItem.category);
        if(newsItem.bitmapsLoaded.size() != 0){
            System.out.println("Show image");
            holder.imageView.setImageBitmap(newsItem.bitmapsLoaded.get(0));
        } else if(!newsItem.downloading) {
            //System.out.println("Downloading");
            newsItem.downloading = true;
            System.out.println("time in adapter[" + position + "]: " + System.currentTimeMillis());
            //threadPoolExecutor.execute(new ThreadPoolDownloadImage(holder.imageView, newsItem, newsItem.pathImages[0], this));
            new DataImageDownloaderTask(holder.imageView, newsItem).execute(newsItem.pathImages[0]);
        } else{
            //System.out.println("Image don\'t downloaded");
            System.out.println(holder.imageView.getDrawable() == null);
            holder.imageView.setImageResource(android.R.color.transparent);
            holder.imageView.setBackgroundColor(0x00ff00000);
        }
        /*if(newsItem.pathImages.length > 0 && !newsItem.downloading){
            System.err.println("position = " + position);
            newsItem.downloading = true;
            new DataImageDownloaderTask(holder.imageView, newsItem).execute(newsItem.pathImages[0]);
            System.out.println("newsItem.bitmapsLoaded.size() = " + newsItem.bitmapsLoaded.size());
        } else if(newsItem.bitmapsLoaded.size() > 0) {
            System.out.println("set image");

        } else {
            holder.imageView.setBackgroundColor(0x00ff00000);
            notifyDataSetChanged();
        }*/

        /*if(position - 10 > values.size()){
            try {
                System.out.println("add new " + values.get(values.size() - 1).id);
                addAll(new DataDownloaderTask().execute("get " + values.get(values.size() - 1).id + " 100").get());
                notifyDataSetChanged();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }*/
        //holder.viewSwitcher.setDisplayedChild(newsItem.state);

        convertView.setOnClickListener(view -> {
            //holder.viewSwitcher.showNext();
            if(values.get(position).state == 1)
                values.get(position).state = 0;
            else
                values.get(position).state = 1;
            //holder.viewSwitcher.showNext();
            //MySimpleArrayAdapter.this.notifyDataSetChanged();
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, ScrollingActivity.class);
                intent.putExtra("data", values.get(position));
                if(values.get(position).bitmapsLoaded.size() > 0) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    values.get(position).bitmapsLoaded.get(0).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    System.out.println(byteArray.length);
                    //intent.putExtra("image", byteArray);

                }
                intent.putExtra("position", position);
                context.startActivity(intent);
                return false;
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView cost;
        public TextView category;
        public ImageView imageView;
        public ViewSwitcher viewSwitcher;
    }

    public class DataImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private final MyData myDataWeakReference;

        public DataImageDownloaderTask(ImageView imageView, MyData myData){
            imageViewWeakReference = new WeakReference<>(imageView);
            myDataWeakReference = myData;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            long time = System.currentTimeMillis();
            System.out.println("doInBackground: " + time);
            int count = -1;
            Bitmap bitmap;
            for(String link : strings){

                try (Socket socket = new Socket("192.168.1.13", 44579);
                     DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                     PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
                    System.out.println("getImage");
                    String giftImages = "";

                    String[] buf;
                    pw.println("getImage " + link);

                    int size= inputStream.readInt();
                    //int size = (int) getLongFromBytes(bytes);
                    System.out.println(size);
                    try (InputStream imageData = new SubStream(inputStream, size)) {
                        bitmap = BitmapFactory.decodeStream(imageData);
                    }
                    System.out.println("time: " + (System.currentTimeMillis() - time));
                    System.out.println("doInBackground done: " + System.currentTimeMillis());
                    return bitmap;
                } catch (IOException e) {

                    e.printStackTrace();
                }
                System.out.println("AsyncTask:" + link);
            }
            return null;
        }



        public long getLongFromBytes(byte[] bytes){
            long value = 0;
            for (int i = 0; i < bytes.length; i++)
            {
                value = (value << 8) + (bytes[i] & 0xff);
            }
            return value;
        }



        @Override
        protected void onPostExecute(Bitmap bitmaps) {
            super.onPostExecute(bitmaps);
            if (imageViewWeakReference != null) {
                ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    if (bitmaps != null) {
                        myDataWeakReference.addBitmap(bitmaps);
                        System.out.println("DataImageDownloaderTask.onPostExecute [" + myDataWeakReference);
                        imageView.setImageBitmap(bitmaps);
                        MySimpleArrayAdapter.this.notifyDataSetChanged();
                    } else {
                        //Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                        imageView.setBackgroundColor(0x00ff0000);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String link) {

            return null;
        }

        private final class SubStream extends FilterInputStream {
            private final long length;
            private long pos;

            public SubStream(final InputStream stream, final long length) {
                super(stream);

                this.length = length;
            }

            @Override
            public boolean markSupported() {
                return false;
            }

            @Override
            public int available() throws IOException {
                return (int) Math.min(super.available(), length - pos);
            }

            @Override
            public int read() throws IOException {
                if (pos++ >= length) {
                    return -1;
                }

                return super.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (pos >= length) {
                    return -1;
                }

                int count = super.read(b, off, (int) Math.min(len, length - pos));

                if (count < 0) {
                    return -1;
                }

                pos += count;

                return count;
            }

            @Override
            public long skip(long n) throws IOException {
                if (pos >= length) {
                    return -1;
                }

                long skipped = super.skip(Math.min(n, length - pos));

                if (skipped < 0) {
                    return -1;
                }

                pos += skipped;

                return skipped;
            }

            @Override
            public void close() throws IOException {
                // Don't close wrapped stream, just consume any bytes left
                while (pos < length) {
                    skip(length - pos);
                }
            }
        }
    }
}
