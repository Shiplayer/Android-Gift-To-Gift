package com.advanced.java.ship.appofdatachanges.downloaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.advanced.java.ship.appofdatachanges.adapters.MySimpleArrayAdapter;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

/**
 * Created by Anton on 17.11.2017.
 */

public class ThreadPoolDownloadImage implements Runnable {
    private WeakReference<ImageView> imageViewWeakReference;
    private WeakReference<MyData> myDataWeakReference;
    private WeakReference<ArrayAdapter<?>> arrayAdapter;
    private String url;

    public ThreadPoolDownloadImage(ImageView imageView, MyData myData, String url, ArrayAdapter arrayAdapter){
        imageViewWeakReference = new WeakReference<>(imageView);
        myDataWeakReference = new WeakReference<>(myData);
        this.arrayAdapter = new WeakReference<ArrayAdapter<?>>(arrayAdapter);
        this.url = url;
    }
    @Override
    public void run() {
        long time = System.currentTimeMillis();
        System.out.println("doInBackground: " + time);
        int count = -1;
        Bitmap bitmap;
        String link = url;
        try (Socket socket = new Socket("192.168.1.13", 44579);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("getImage");
            String giftImages = "";

            String[] buf;
            pw.println("getImage " + link);

            int size = inputStream.readInt();
            //int size = (int) getLongFromBytes(bytes);
            System.out.println(size);
            try (InputStream imageData = new DataImageDownloaderTasks.SubStream(inputStream, size)) {
                bitmap = BitmapFactory.decodeStream(imageData);
            }


            System.out.println("time: " + (System.currentTimeMillis() - time));
            System.out.println("doInBackground done: " + System.currentTimeMillis());
            setImage(bitmap);
        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println("AsyncTask:" + link);
        //bitmaps.add(downloadBitmap(link));
    }
        //System.out.println(bitmaps.isEmpty() + " " + bitmaps.size() + " " + bitmaps.get(0).getHeight() + "x" + bitmaps.get(0).getWidth());
        //imageViewWeakReference.get().setImageBitmap(bitmaps.get(0));

    public void setImage(Bitmap bitmaps) {
        if (imageViewWeakReference != null && myDataWeakReference != null && arrayAdapter != null) {
            ImageView imageView = imageViewWeakReference.get();
            MyData myData = myDataWeakReference.get();
            if (imageView != null) {
                if (bitmaps != null) {
                    myData.addBitmap(bitmaps);
                    System.out.println("DataImageDownloaderTask.onPostExecute [" + myDataWeakReference);
                    imageView.setImageBitmap(bitmaps);
                } else {
                    //Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    imageView.setBackgroundColor(0x00ff0000);
                }
            }
            arrayAdapter.get().notifyDataSetChanged();
        }
    }
}
