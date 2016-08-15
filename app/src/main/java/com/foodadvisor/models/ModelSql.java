package com.foodadvisor.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.foodadvisor.DAL.LastUpdateSql;
import com.foodadvisor.DAL.RestaurantSql;

/**
 * Created by Ori on 15/08/2016.
 */

public class ModelSql {
    final static int VERSION = 6;

    Helper sqlDb;


    public ModelSql(Context context){
        sqlDb = new Helper(context);
    }

    public SQLiteDatabase getWritableDB(){
        return sqlDb.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB(){
        return sqlDb.getReadableDatabase();
    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            RestaurantSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            RestaurantSql.drop(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
