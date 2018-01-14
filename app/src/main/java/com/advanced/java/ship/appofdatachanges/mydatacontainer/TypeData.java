package com.advanced.java.ship.appofdatachanges.mydatacontainer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Anton on 23.11.2017.
 */

public abstract class TypeData {
    public static final int ITEM = 0;
    public static final int CATEGORY = 1;
    public static final int DEFAULT = 2;
    public final int type;

    TypeData(@Type int type){
        this.type = type;
    }

    public @Type int getType(){
        return type;
    }

    public int setType(@Type int type){
        return type;
    }

    @IntDef({ITEM, CATEGORY, DEFAULT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type{    }
}
