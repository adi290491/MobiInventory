package com.example.android.mobiinventory.orders.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.mobiinventory.customers.data.CustomerInventoryContract;
import com.example.android.mobiinventory.orders.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya.sawant on 07-09-2017.
 */

public final class OrdersDataUtils {

    public static void insertOrders(final Context context, List<Order> ordersList){
        OrderDBHelper dbHelper = new OrderDBHelper(context);
        long rowId = 0;
        for(Order order : ordersList){
            ContentValues values = new ContentValues();
            values.put(CustomerInventoryContract.OrderEntry.COLUMN_ORDER_ID, order.getOrderId());
            values.put(CustomerInventoryContract.OrderEntry.COLUMN_CUSTOMER_ID, order.getCustomerId());
            values.put(CustomerInventoryContract.OrderEntry.COLUMN_REQUIRED_DATE, order.getRequiredDate());
            values.put(CustomerInventoryContract.OrderEntry.COLUMN_SHIP_ADDRESS, order.getShipAddress());
            values.put(CustomerInventoryContract.OrderEntry.COLUMN_SHIP_COUNTRY, order.getShipCountry());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            rowId = db.insert(CustomerInventoryContract.OrderEntry.TABLE_NAME, null, values);
        }
        if(rowId!=-1){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Orders Inserted successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Orders Inserted failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static List<Order> fetchAllOrders(Context context, String customerId){
        List<Order> orderList = new ArrayList<>();
        OrderDBHelper dbHelper = new OrderDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = CustomerInventoryContract.OrderEntry.COLUMN_CUSTOMER_ID + "=?";
        String selectionArg[] = {customerId};
        Cursor cursor = db.query(CustomerInventoryContract.OrderEntry.TABLE_NAME,
                null,
                selection,
                selectionArg,
                null,
                null,
                null);
        while(cursor.moveToNext()){
            int orderId = cursor.getInt(cursor.getColumnIndex(CustomerInventoryContract.OrderEntry.COLUMN_ORDER_ID));
            String requiredDate = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.OrderEntry.COLUMN_REQUIRED_DATE));
            String shipAddress = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.OrderEntry.COLUMN_SHIP_ADDRESS));
            String shipCountry = cursor.getString(cursor.getColumnIndex(CustomerInventoryContract.OrderEntry.COLUMN_SHIP_COUNTRY));

            Order order = new Order(orderId, customerId, requiredDate, shipAddress, shipCountry);
            orderList.add(order);
        }
        return orderList;
    }

    public static boolean isOrderDBEmptyForCustomer(Context context, String customerId){
        OrderDBHelper dbHelper = new OrderDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = CustomerInventoryContract.OrderEntry.COLUMN_CUSTOMER_ID + "=?";
        String selectionArg[] = {customerId};
        Cursor cursor = db.query(CustomerInventoryContract.OrderEntry.TABLE_NAME,
                null,
                selection,
                selectionArg,
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
