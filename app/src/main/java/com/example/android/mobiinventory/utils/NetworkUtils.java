package com.example.android.mobiinventory.utils;

import android.content.Context;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by aditya.sawant on 06-09-2017.
 */

public final class NetworkUtils {

    public static URL buildURI(Context context){
        Uri uri = Uri.parse(UriManager.CUSTOMER_API).buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURI(Context context, String customerId){
        Uri uri = Uri.parse(UriManager.ORDER_API+customerId).buildUpon().build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
