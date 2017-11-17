package com.advanced.java.ship.appofdatachanges.adapters;

import android.content.Context;
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
import com.advanced.java.ship.appofdatachanges.downloaders.DataDownloaderTask;
import com.advanced.java.ship.appofdatachanges.downloaders.DataImageDownloaderTasks;
import com.advanced.java.ship.appofdatachanges.downloaders.ThreadPoolDownloadImage;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

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
            //System.out.println("Show image");
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
            /*BufferedImage bufferedImage = ImageIO.read(inputStream);
            System.out.println(bufferedImage.getHeight() + " " + bufferedImage.getWidth());

            */


                /*count = inputStream.read(countBytes);
                int x = ByteBuffer.wrap(countBytes).getInt();
                //inputStream.read
                int value = ((countBytes[0] & 0xFF) << 24) | ((countBytes[1] & 0xFF) << 16)
                        | ((countBytes[2] & 0xFF) << 8) | (countBytes[3] & 0xFF);
                System.out.println(x + " " + count + " " + value);
                byte[] bytes = new byte[count];
                int len = inputStream.read(bytes, 0, count);
                System.out.println(x + " " + value);

                bytes = new byte[value];
                count = 0;
                int off = 0;

                System.out.println("available" + inputStream.available());
                while((off = inputStream.read(bytes)) != -1){
                    count += off;
                    System.out.println("while: " + off);
                    System.out.println("available while: " + inputStream.available());
                }*/

                /*while((off = inputStream.read(bytes, count, bytes.length - count - 1)) != 0){
                    count += off;
                    System.out.println("while: " + count);
                }*/
                    //count += inputStream.read(bytes, count, bytes.length);
                /*System.err.println("count: " + bytes.length);



                //bitmaps.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                System.out.println(mutableBitmap.getWidth() + " " + mutableBitmap.getHeight());
                */


                /*byte[] sizeBytes = new byte[Long.BYTES];
                inputStream.read(sizeBytes);
                long value = getLongFromBytes(sizeBytes);
                System.out.println("value = " + value);
                int bytesSize = 4096;
                byte[] bytes = new byte[bytesSize];
                int total = 0, offset = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                count = 0;
                int len = bytesSize;
                while((count = inputStream.read(bytes, offset, len - offset)) != -1){
                    System.out.println("count = " + count);
                    if(count + offset < bytesSize){
                        offset = count;
                        System.out.println("info = " + count + " " + total);
                    } else {
                        offset = 0;
                    }
                    total += count;
                    System.out.println("total = " + total);
                    byteArrayOutputStream.write(bytes, 0, count);
                    System.out.println(len - offset);

                }
                byte[] bitmapBytes = byteArrayOutputStream.toByteArray();
                System.out.println("byteArrayOutputStream.toByteArray().length = [" + byteArrayOutputStream.toByteArray().length + "]");
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);*/
                    System.out.println("time: " + (System.currentTimeMillis() - time));
                    System.out.println("doInBackground done: " + System.currentTimeMillis());
                    return bitmap;
                } catch (IOException e) {

                    e.printStackTrace();
                }
                System.out.println("AsyncTask:" + link);
                //bitmaps.add(downloadBitmap(link));
            }
            //System.out.println(bitmaps.isEmpty() + " " + bitmaps.size() + " " + bitmaps.get(0).getHeight() + "x" + bitmaps.get(0).getWidth());
            //imageViewWeakReference.get().setImageBitmap(bitmaps.get(0));
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
        /*MyData myData = myDataWeakReference.get();
        System.out.println(myData);
        try (Socket socket = new Socket("192.168.1.13", 44579);
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("getImage");
            String giftImages = "";

            String[] buf;
            pw.println("getImage " + link);

            int count = inputStream.readInt();
            byte[] bytes = new byte[count];
            int len = inputStream.read(bytes);
            if(count != len){
                System.err.println(count + " != " + len);
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            myData.addBitmap(bitmap);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("error");*/

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
