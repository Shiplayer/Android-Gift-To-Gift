package com.advanced.java.ship.appofdatachanges.mydatacontainer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton on 30.10.2017.
 */

public class MyData extends TypeData implements Parcelable {

    public int id;
    public String name;
    public String cost;
    public String category;
    public String description;
    public int state;
    public String[] pathImages;
    public ArrayList<Bitmap> bitmapsLoaded;
    public Drawable imageDrawable;
    public boolean downloading;

    public MyData(int id, String name, String cost, String description, String category, String path){
        super(TypeData.ITEM);
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.description = description;
        this.state = 0;
        path = path.substring(1, path.length() - 1);
        bitmapsLoaded = new ArrayList<>();
        imageDrawable = null;
        downloading = false;
        pathImages = path.split(", ");
        if(pathImages.length != 0) {
            System.out.println("construct MyData: " + Arrays.toString(pathImages));
        } else {
            System.out.println("construct MyData: " + toString());
        }
    }

    public MyData(int id, String name, String cost, String description, String category, Drawable drawable){
        super(TypeData.DEFAULT);
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.description = description;
        this.state = 0;
        bitmapsLoaded = new ArrayList<>();
        imageDrawable = drawable;
        bitmapsLoaded.add(drawableToBitmap(drawable));
        downloading = false;
        pathImages = "hello".split("e");
        if(pathImages.length != 0) {
            System.out.println("construct MyData: " + Arrays.toString(pathImages));
        } else {
            System.out.println("construct MyData: " + toString());
        }
    }

    public MyData(){
        super(TypeData.ITEM);
        this.id = -1;
        this.name = "error";
        this.cost = "error";
        this.category = "error";
        this.description = "error";
        this.state = 0;
        bitmapsLoaded = new ArrayList<>();
        downloading = false;
        pathImages = null;
    }

    protected MyData(Parcel in) {
        super(in.readInt());
        id = in.readInt();
        name = in.readString();
        cost = in.readString();
        category = in.readString();
        description = in.readString();
        state = in.readInt();
        //pathImages = (String[]) in.readArray();
        downloading = in.readByte() != 0;
        bitmapsLoaded = new ArrayList<>();

        /*

        parcel.writeArray(pathImages);
         */
        //bitmapsLoaded.add(in.readParcelable(Bitmap.class.getClassLoader()));

    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = null;
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        return bitmap;
    }

    public void addBitmap(Bitmap bitmap){
        bitmapsLoaded.add(bitmap);
    }

    public void addBitmapAsDrawable(Drawable drawable){
        bitmapsLoaded.add(drawableToBitmap(drawable));
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


    /*
    public int id;
    public String name;
    public String cost;
    public String category;
    public int state;
    public String[] pathImages;
    public ArrayList<Bitmap> bitmapsLoaded;
    public boolean downloading;
     */
    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(type);
        parcel.writeInt(id); // id = in.readInt();
        parcel.writeString(name); // name = in.readString();
        parcel.writeString(cost); // cost = in.readString();
        parcel.writeString(category); // category = in.readString();
        parcel.writeString(description); //description = in.readString();
        parcel.writeInt(state); //state = in.readInt();
        parcel.writeByte((byte) (downloading ? 1 : 0));
        //parcel.writeArray(pathImages); ////pathImages = (String[]) in.readArray();
        //parcel.writeParcelable(bitmapsLoaded.get(0), flag);
        //parcel.writeList(bitmapsLoaded); bitmapsLoaded = new ArrayList<>();
        //parcel.writeValue(downloading); downloading = false;
    }
}
