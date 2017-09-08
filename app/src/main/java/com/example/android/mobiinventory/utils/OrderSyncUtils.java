package com.example.android.mobiinventory.utils;

import android.content.Context;
import android.util.Base64;

import com.example.android.mobiinventory.orders.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.android.mobiinventory.utils.MobiInventoryUtils.getCurrentLocationCoordinates;

/**
 * Created by aditya.sawant on 08-09-2017.
 */

public final class OrderSyncUtils {

    public static String fetchOrderForCustomer(Context context, URL url) throws IOException {
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        String userCredentials = "admin:admin";
        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), 0);
        httpUrlConnection.setRequestProperty("Authorization", basicAuth);
        httpUrlConnection.setRequestMethod("GET");
        httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpUrlConnection.setRequestProperty("Content-Language", "en-US");
        double latlng[] = getCurrentLocationCoordinates(context);
        httpUrlConnection.setRequestProperty("lat", Double.toString(latlng[0]));
        httpUrlConnection.setRequestProperty("long", Double.toString(latlng[1]));

        InputStream in = httpUrlConnection.getInputStream();
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");
        boolean hasInput = scanner.hasNext();
        String response = null;
        if (hasInput) {
            response = scanner.next();
        }
        scanner.close();
        return response;
    }

    public static List<Order> fetchOrderList(String orderJsonString){
        List<Order> orderList = new ArrayList<>();
        try {
            JSONArray orderJsonArray = new JSONArray(orderJsonString);
            for(int i=0; i<orderJsonArray.length();i++){
                JSONObject orderObject = orderJsonArray.getJSONObject(i);
                int orderId = orderObject.getInt("OrderID");
                String customerId = orderObject.getString("CustomerID");
                String requiredDate = orderObject.getString("RequiredDate");
                String shipAddress = orderObject.getString("ShipAddress");
                String shipCountry = orderObject.getString("ShipCountry");

                String formattedDateString = MobiInventoryUtils.getFormattedDate(requiredDate);
                Order order = new Order(orderId, customerId, formattedDateString, shipAddress, shipCountry);
                orderList.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orderList;
    }
}
