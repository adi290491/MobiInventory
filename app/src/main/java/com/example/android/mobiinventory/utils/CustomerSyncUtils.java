package com.example.android.mobiinventory.utils;

import android.content.Context;
import android.util.Base64;

import com.example.android.mobiinventory.customers.CustomerInventory;

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


/**
 * Created by aditya.sawant on 07-09-2017.
 */

public class CustomerSyncUtils {

    public static String fetchAllCustomers(Context context, URL url) throws IOException {

        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        String userCredentials = "admin:admin";
        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), 0);
        httpUrlConnection.setRequestProperty("Authorization", basicAuth);
        httpUrlConnection.setRequestMethod("GET");
        httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpUrlConnection.setRequestProperty("Content-Language", "en-US");
        double latlng[] = MobiInventoryPreferences.getLocationCoordinates(context);
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


    public static List<CustomerInventory> fetchCustomerList(String customerList) {
        List<CustomerInventory> customerInventoryList = new ArrayList<>();
        try {
            JSONArray customerJsonArray = new JSONArray(customerList);
            for(int i=0; i < customerJsonArray.length();i++){
                JSONObject customerJsonObject = customerJsonArray.getJSONObject(i);
                String customerId = customerJsonObject.getString("CustomerID");
                String companyName = customerJsonObject.getString("CompanyName");
                String contactName = customerJsonObject.getString("ContactName");
                String postalCode = customerJsonObject.getString("PostalCode");
                String country = customerJsonObject.getString("Country");
                String phone = customerJsonObject.getString("Phone");

                CustomerInventory ci = new CustomerInventory(customerId, contactName, companyName, country, postalCode, phone);
                customerInventoryList.add(ci);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerInventoryList;
    }
}
