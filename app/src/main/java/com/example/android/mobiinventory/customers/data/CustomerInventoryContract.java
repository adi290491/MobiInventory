package com.example.android.mobiinventory.customers.data;

import android.provider.BaseColumns;

/**
 * Created by aditya.sawant on 07-09-2017.
 */

public final class CustomerInventoryContract {

    public class CustomerEntry implements BaseColumns{
        public static final String TABLE_NAME = "CUSTOMERS";
        public static final String COLUMN_CUSTOMER_ID = "CUSTOMER_ID";
        public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
        public static final String COLUMN_COMPANY_NAME = "COMPANY_NAME";
        public static final String COLUMN_COUNTRY = "COUNTRY";
        public static final String COLUMN_POSTAL_CODE = "POSTAL_CODE";
        public static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    }

    public class OrderEntry implements BaseColumns{
        public static final String TABLE_NAME = "ORDERS";
        public static final String COLUMN_ORDER_ID = "ORDER_ID";
        public static final String COLUMN_CUSTOMER_ID = "CUSTOMER_ID";
        public static final String COLUMN_REQUIRED_DATE = "REQUIRED_DATE";
        public static final String COLUMN_SHIP_ADDRESS = "SHIP_ADDRESS";
        public static final String COLUMN_SHIP_COUNTRY = "SHIP_COUNTRY";
    }
}
