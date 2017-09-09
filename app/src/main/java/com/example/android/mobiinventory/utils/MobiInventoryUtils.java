package com.example.android.mobiinventory.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aditya.sawant on 08-09-2017.
 */

public final class MobiInventoryUtils {
    private static final int REQUEST_LOCATION_PERMISSION = 1000;
    private static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    private static final String FORMAT_YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'hh:mm:ss";



    public static String getFormattedDate(String requiredDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYYMMDDTHHMMSS);
        try {
            Date date = sdf.parse(requiredDate);
            SimpleDateFormat friendlyformat = new SimpleDateFormat(FORMAT_YYYYMMDD);
            return friendlyformat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
