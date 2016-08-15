package com.foodadvisor.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foodadvisor.models.Restaurant;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 08/06/2016.
 */
public class RestaurantSql {

    final static String RESTAURANT_TABLE = "restaurants";
    final static String RESTAURANT_TABLE_ID = "id";
    final static String RESTAURANT_TABLE_NAME = "name";
    final static String RESTAURANT_TABLE_ACCESSIBLE = "accessible";
    final static String RESTAURANT_TABLE_ADDRESS = "address";
    final static String RESTAURANT_TABLE_PARKING = "parking";
    final static String RESTAURANT_TABLE_PHONE = "phone";
    final static String RESTAURANT_TABLE_KOSHER = "kosher";
    final static String RESTAURANT_TABLE_TYPE = "type";
    final static String RESTAURANT_TABLE_LONGITUDE = "longitude";
    final static String RESTAURANT_TABLE_LATITUDE = "latitude";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + RESTAURANT_TABLE + " (" +
                RESTAURANT_TABLE_ID + " INTEGER PRIMARY KEY," +
                RESTAURANT_TABLE_NAME + " TEXT," +
                RESTAURANT_TABLE_ACCESSIBLE + " BOOLEAN," +
                RESTAURANT_TABLE_ADDRESS + " TEXT," +
                RESTAURANT_TABLE_PARKING + " BOOLEAN," +
                RESTAURANT_TABLE_PHONE + " TEXT," +
                RESTAURANT_TABLE_TYPE + " TEXT," +
                RESTAURANT_TABLE_LONGITUDE + " REAL," +
                RESTAURANT_TABLE_LATITUDE + " REAL," +
                RESTAURANT_TABLE_KOSHER + " BOOLEAN);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + RESTAURANT_TABLE + ";");
    }

    public static List<Restaurant> getAllRestaurants(SQLiteDatabase db) {
        Cursor cursor = db.query(RESTAURANT_TABLE, null, null , null, null, null, null);
        List<Restaurant> students = new LinkedList<Restaurant>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(RESTAURANT_TABLE_ID);
            int nameIndex = cursor.getColumnIndex(RESTAURANT_TABLE_NAME);
            int accessibleIndex = cursor.getColumnIndex(RESTAURANT_TABLE_ACCESSIBLE);
            int addressIndex = cursor.getColumnIndex(RESTAURANT_TABLE_ADDRESS);
            int parkingIndex = cursor.getColumnIndex(RESTAURANT_TABLE_PARKING);
            int phoneIndex = cursor.getColumnIndex(RESTAURANT_TABLE_PHONE);
            int typeIndex = cursor.getColumnIndex(RESTAURANT_TABLE_TYPE);
            int longIndex = cursor.getColumnIndex(RESTAURANT_TABLE_LONGITUDE);
            int latIndex = cursor.getColumnIndex(RESTAURANT_TABLE_LATITUDE);
            int kosherIndex = cursor.getColumnIndex(RESTAURANT_TABLE_KOSHER);
            do {
                Integer id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String address = cursor.getString(addressIndex);
                String type = cursor.getString(typeIndex);
                Double longitude = cursor.getDouble(longIndex);
                Double latitude = cursor.getDouble(latIndex);
                String phone = cursor.getString(phoneIndex);
                int accessible = cursor.getInt(accessibleIndex); //0 false / 1 true
                int kosher = cursor.getInt(kosherIndex); //0 false / 1 true
                int parking = cursor.getInt(parkingIndex); //0 false / 1 true
                Restaurant st = new Restaurant(id, name, address, type, kosher  == 1, phone, parking  == 1, accessible == 1, latitude, longitude);
                students.add(st);
            } while (cursor.moveToNext());
        }
        return students;
    }

//    public static Student getStudentById(SQLiteDatabase db, String id) {
//        String where = STUDENT_TABLE_ID + " = ?";
//        String[] args = {id};
//        Cursor cursor = db.query(STUDENT_TABLE, null, where, args, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(STUDENT_TABLE_ID);
//            int fnameIndex = cursor.getColumnIndex(STUDENT_TABLE_FNAME);
//            int lnameIndex = cursor.getColumnIndex(STUDENT_TABLE_LNAME);
//            int addressIndex = cursor.getColumnIndex(STUDENT_TABLE_ADDRESS);
//            int imageNameIndex = cursor.getColumnIndex(STUDENT_TABLE_IMAGE_NAME);
//            int phoneIndex = cursor.getColumnIndex(STUDENT_TABLE_PHONE);
//            int checkableIndex = cursor.getColumnIndex(STUDENT_TABLE_CHECKABLE);
//            String _id = cursor.getString(idIndex);
//            String fname = cursor.getString(fnameIndex);
//            String lname = cursor.getString(lnameIndex);
//            String address = cursor.getString(addressIndex);
//            String imageName = cursor.getString(imageNameIndex);
//            String phone = cursor.getString(phoneIndex);
//            int checkable = cursor.getInt(checkableIndex); //0 false / 1 true
//            Student st = new Student(_id, fname, lname,phone, address, imageName, checkable == 1);
//            return st;
//        }
//        return null;
//    }

    public static void add(SQLiteDatabase db, Restaurant st) {
        ContentValues values = new ContentValues();
        values.put(RESTAURANT_TABLE_ID, st.getId());
        values.put(RESTAURANT_TABLE_ADDRESS, st.getAddress());
        values.put(RESTAURANT_TABLE_LATITUDE, st.getLatitude());
        values.put(RESTAURANT_TABLE_LONGITUDE, st.getLongitude());
        values.put(RESTAURANT_TABLE_NAME, st.getName());
        values.put(RESTAURANT_TABLE_PHONE, st.getPhone());
        values.put(RESTAURANT_TABLE_TYPE, st.getType());
        if (st.getKosher()) {
            values.put(RESTAURANT_TABLE_KOSHER, 1);
        } else {
            values.put(RESTAURANT_TABLE_KOSHER, 0);
        }
        if (st.getParking()) {
            values.put(RESTAURANT_TABLE_PARKING, 1);
        } else {
            values.put(RESTAURANT_TABLE_PARKING, 0);
        }
        if (st.getAccessible()) {
            values.put(RESTAURANT_TABLE_ACCESSIBLE, 1);
        } else {
            values.put(RESTAURANT_TABLE_ACCESSIBLE, 0);
        }
        db.insertWithOnConflict(RESTAURANT_TABLE, RESTAURANT_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,RESTAURANT_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, String date){
        LastUpdateSql.setLastUpdate(db,RESTAURANT_TABLE, date);
    }
}
