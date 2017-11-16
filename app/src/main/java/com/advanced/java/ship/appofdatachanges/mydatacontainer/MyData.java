package com.advanced.java.ship.appofdatachanges.mydatacontainer;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 30.10.2017.
 */

public class MyData implements Parcelable {
    public int id;
    public String name;
    public String cost;
    public String category;
    public int state;
    public String[] pathImages;
    public ArrayList<Bitmap> bitmapsLoaded;
    public boolean downloading;

    public MyData(int id, String name, String cost, String category, String path){
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.state = 0;
        System.out.println(path);
        path = path.substring(1, path.length() - 1);
        pathImages = path.split(", ");
        System.out.println(Arrays.toString(pathImages));
        bitmapsLoaded = new ArrayList<>();
        downloading = false;
    }

    protected MyData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        cost = in.readString();
        category = in.readString();
        state = in.readInt();
        in.readTypedList(bitmapsLoaded, Bitmap.CREATOR);
    }

    public void addBitmap(Bitmap bitmap){
        bitmapsLoaded.add(bitmap);
    }

    public void addBitmapList(List<Bitmap> bitmapList){
        bitmapsLoaded.addAll(bitmapList);
    }

    public static final Creator<MyData> CREATOR = new Creator<MyData>() {
        @Override
        public MyData createFromParcel(Parcel in) {
            return new MyData(in);
        }

        @Override
        public MyData[] newArray(int size) {
            return new MyData[size];
        }
    };

    @Override
    public String toString() {
        return "[id = " + id + ", name = " + name + ", cost = " + cost + ", category = " + category + ", state = " + state + ", pathImages.length = "
                + pathImages.length + ", bitmapsLoaded.size() = " + bitmapsLoaded.size() + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(cost);
        parcel.writeString(category);
        parcel.writeInt(state);
        parcel.writeList(bitmapsLoaded);
    }
}
