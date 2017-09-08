package com.example.android.mobiinventory.customers.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.mobiinventory.customers.CustomerInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.sawant on 07-09-2017.
 */

public final class CustomerDataUtils {

    public static void insertCustomers(final Context context, List<CustomerInventory> customerInventoryList){
        CustomerInventoryDBHelper dbHelper = new CustomerInventoryDBHelper(context);
        long rowId = 0;
        for(CustomerInventory ci : customerInventoryList){
            ContentValues values = new ContentValues();
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_ID, ci.getCustomerId());
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_NAME, ci.getCustomerName());
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_COMPANY_NAME, ci.getCompanyName());
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_COUNTRY, ci.getCountry());
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_POSTAL_CODE, ci.getPostalCode());
            values.put(CustomerInventoryContract.CustomerEntry.COLUMN_PHONE_NUMBER, ci.getPhoneNumber());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            rowId = db.insert(CustomerInventoryContract.CustomerEntry.TABLE_NAME, null, values);
        }
        if(rowId!=-1){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Customers Inserted successfully", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Customers Inserted failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static List<CustomerInventory> fetchAllCustomers(Context context){
        List<CustomerInventory> customerInventories = new ArrayList<>();
        CustomerInventoryDBHelper dbHelper = new CustomerInventoryDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(CustomerInventoryContract.CustomerEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        while(cursor.moveToNext()){
            String customerId = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_ID));
            String customerName = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_CUSTOMER_NAME));
            String companyName = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_COMPANY_NAME));
            String country = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_COUNTRY));
            String postalCode = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_POSTAL_CODE));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.CustomerEntry.COLUMN_PHONE_NUMBER));
            CustomerInventory ci = new CustomerInventory(customerId, customerName, companyName, country, postalCode, phoneNumber);
            customerInventories.add(ci);
        }
        return customerInventories;
    }

    public static boolean isCustomerDBEmpty(Context context){
        CustomerInventoryDBHelper dbHelper = new CustomerInventoryDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(CustomerInventoryContract.CustomerEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return true;
        }
        return false;
    }
}
