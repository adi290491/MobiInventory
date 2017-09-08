package com.example.android.mobiinventory.orders.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.mobiinventory.customers.data.CustomerInventoryContract;

/**
 * Created by aditya.sawant on 07-09-2017.
 */

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "order.db";
    private static final int DATABASE_VERSION = 1;

    public OrderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + CustomerInventoryContract.OrderEntry.TABLE_NAME + "(" +
                CustomerInventoryContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CustomerInventoryContract.OrderEntry.COLUMN_ORDER_ID + " INTEGER, " +
                CustomerInventoryContract.OrderEntry.COLUMN_CUSTOMER_ID + " TEXT, " +
                CustomerInventoryContract.OrderEntry.COLUMN_REQUIRED_DATE + " TEXT, " +
                CustomerInventoryContract.OrderEntry.COLUMN_SHIP_ADDRESS + " TEXT, " +
                CustomerInventoryContract.OrderEntry.COLUMN_SHIP_COUNTRY + " TEXT);";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CustomerInventoryContract.OrderEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
