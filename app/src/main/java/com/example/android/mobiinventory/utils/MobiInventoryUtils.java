package com.example.android.mobiinventory.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

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
    public static double[] getCurrentLocationCoordinates(Context context) {
        double latlng[] = new double[2];
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        latlng[0] = location.getLatitude();
        latlng[1] = location.getLongitude();
        return latlng;
    }

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
