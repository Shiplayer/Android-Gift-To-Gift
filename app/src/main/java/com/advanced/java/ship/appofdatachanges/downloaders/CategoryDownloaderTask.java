package com.advanced.java.ship.appofdatachanges.downloaders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.advanced.java.ship.appofdatachanges.R;
import com.advanced.java.ship.appofdatachanges.mydatacontainer.MyData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 01.12.2017.
 */

public class CategoryDownloaderTask extends AsyncTask<String, Void, List<String>> {
    private Context context;

    public CategoryDownloaderTask(Context context){
        this.context = context;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        String[] result;
        try (Socket socket = new Socket("192.168.1.13", 44579);
             BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)){

            pw.println(strings[0]);
            String line = bf.readLine();
            result = line.split(";");
            Log.w("asynctask", String.valueOf(result.length));
            Log.w("asynctask", line);

        } catch (IOException e) {
            e.printStackTrace();
            result = context.getResources().getStringArray(R.array.default_categories_in_menu);
        }
        Log.w("result asynctask", String.valueOf(result.length));
        return Arrays.asList(result);
    }
}
