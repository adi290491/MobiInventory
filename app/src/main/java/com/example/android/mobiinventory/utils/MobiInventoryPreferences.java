package com.example.android.mobiinventory.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 08-09-2017.
 */

public class MobiInventoryPreferences {
    public static final String PREF_COORD_LAT = "coord_lat";
    public static final String PREF_COORD_LONG = "coord_long";

    public static void setLocationDetails(Context context, double lat, double lon){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(PREF_COORD_LAT, Double.doubleToRawLongBits(lat));
        editor.putLong(PREF_COORD_LONG, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    public static void resetLocationCoordinates(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(PREF_COORD_LAT);
        editor.remove(PREF_COORD_LONG);
        editor.apply();
    }

    public static double[] getLocationCoordinates(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        double[] coords = new double[2];

        coords[0] = Double.longBitsToDouble(sp.getLong(PREF_COORD_LAT, Double.doubleToRawLongBits(0.0)));
        coords[1] = Double.longBitsToDouble(sp.getLong(PREF_COORD_LONG, Double.doubleToRawLongBits(0.0)));
        return coords;
    }
}
