package com.advanced.java.ship.appofdatachanges.downloaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 01.11.2017.
 */

public class DataImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;
    private final MyData myDataWeakReference;

    public DataImageDownloaderTask(ImageView imageView, MyData myData){
        imageViewWeakReference = new WeakReference<>(imageView);
        myDataWeakReference = myData;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        int count = -1;
        Bitmap bitmap;
        for(String link : strings){

            try (Socket socket = new Socket("192.168.1.13", 44579);
                 InputStream inputStream = socket.getInputStream();
                 PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("getImage");
                String giftImages = "";

                String[] buf;
                pw.println("getImage " + link);
                byte[] countBytes = new byte[4];
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
                byte[] sizeBytes = new byte[4];
                inputStream.read(sizeBytes);
                int value = ((sizeBytes[0] & 0xFF) << 24) | ((sizeBytes[1] & 0xFF) << 16)
                        | ((sizeBytes[2] & 0xFF) << 8) | (sizeBytes[3] & 0xFF);
                System.out.println("value = " + value);
                int bytesSize = 4096;
                byte[] bytes = new byte[bytesSize];
                int total = 0, offset = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                count = 0;
                while((count = inputStream.read(bytes, offset, bytesSize - offset)) != -1){
                    System.out.println("count = " + count);
                    if(count + offset < bytesSize){
                        offset = count;
                    } else {
                        offset = 0;
                    }
                    total += count;
                    byteArrayOutputStream.write(bytes, 0, count);

                }
                byte[] bitmapBytes = byteArrayOutputStream.toByteArray();
                System.out.println("byteArrayOutputStream.toByteArray().length = [" + byteArrayOutputStream.toByteArray().length + "]");
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

                return bitmap;
            } catch (IOException e) {
                System.err.println(count);
                e.printStackTrace();
            }
            System.out.println(link);
            //bitmaps.add(downloadBitmap(link));
        }
        //System.out.println(bitmaps.isEmpty() + " " + bitmaps.size() + " " + bitmaps.get(0).getHeight() + "x" + bitmaps.get(0).getWidth());
        //imageViewWeakReference.get().setImageBitmap(bitmaps.get(0));
        return null;
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
}
