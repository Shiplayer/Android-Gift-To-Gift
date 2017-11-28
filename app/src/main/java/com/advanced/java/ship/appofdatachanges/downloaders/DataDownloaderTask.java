package com.advanced.java.ship.appofdatachanges.downloaders;

import android.os.AsyncTask;

import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anton on 01.11.2017.
 */

public class DataDownloaderTask extends AsyncTask<String, Void, List<MyData>> {
    @Override
    protected List<MyData> doInBackground(String... strings) {
        List<MyData> list = new ArrayList<>();
        try (Socket socket = new Socket("192.168.1.13", 44579);
             BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)){
            String giftContent = "";
            String giftImages = "";
            String[] buf;
            for(int i = 0; i < strings.length; i++){
                pw.println(strings[i]);

                int count = Integer.parseInt(bf.readLine());
                for(int j = 0; j < count; j++){
                    giftContent = bf.readLine();
                    buf = giftContent.split("\t");
                    giftImages = bf.readLine();
                    list.add(new MyData(Integer.parseInt(buf[0]), buf[1], buf[5], buf[2], buf[3], giftImages));

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.isEmpty())
            return null;
        else
            return list;
    }
}
