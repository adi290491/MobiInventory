package com.example.android.mobiinventory.customers.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aditya.sawant on 07-09-2017.
 */

public class CustomerInventoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "customers.db";
    private static final int DATABASE_VERSION = 1;

    public CustomerInventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + CustomerInventoryContract.CustomerEntry.TABLE_NAME + "(" +
                CustomerInventoryContract.CustomerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_ID + " TEXT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_NAME + " TEXT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_COMPANY_NAME + " TEXT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_COUNTRY + " TEXT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_POSTAL_CODE + " TEXT, " +
                CustomerInventoryContract.CustomerEntry.COLUMN_PHONE_NUMBER + " TEXT);";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CustomerInventoryContract.CustomerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
