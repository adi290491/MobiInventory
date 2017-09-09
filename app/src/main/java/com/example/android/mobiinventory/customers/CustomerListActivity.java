package com.example.android.mobiinventory.customers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mobiinventory.R;
import com.example.android.mobiinventory.customers.data.CustomerDataUtils;
import com.example.android.mobiinventory.orders.OrderListActivity;
import com.example.android.mobiinventory.orders.data.OrdersDataUtils;
import com.example.android.mobiinventory.utils.Constants;
import com.example.android.mobiinventory.utils.CustomerSyncUtils;
import com.example.android.mobiinventory.utils.MobiInventoryPreferences;
import com.example.android.mobiinventory.utils.NetworkUtils;
import com.example.android.mobiinventory.utils.SnackbarWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class CustomerListActivity extends AppCompatActivity implements CustomerInventoryAdapter.CustomerSelectListener {

    private static final String LOG_TAG = CustomerListActivity.class.getSimpleName();
    private RecyclerView mCustomerInventoryRecyclerView;
    private CustomerInventoryAdapter mCustomerInventoryAdapter;
    private TextView tvEmptyView;
    private List<CustomerInventory> mCustomerInventoryList = new ArrayList<>();
    private ProgressBar loadingProgressBar;
    private static final int REQUEST_LOCATION_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fetchCustomers();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocationCoordinates(this);
                    //fetchCustomers();
                }
        }
    }

    public void setCurrentLocationCoordinates(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        MobiInventoryPreferences.setLocationDetails(this, location.getLatitude(), location.getLongitude());

    }

    public void fetchCustomers() {

        if (CustomerDataUtils.isCustomerDBEmpty(this)) {
            if (NetworkUtils.isNetworkAvailable(this)) {
                Toast.makeText(this, "Fetching from network", Toast.LENGTH_SHORT).show();
                new FetchCustomerTask().execute();
            } else {
                hideLoading();
               /* Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), "No Connection", Snackbar.LENGTH_SHORT);
                snackbar.show();*/
                Toast.makeText(this, R.string.unable_to_reach_server, Toast.LENGTH_SHORT).show();
                showEmptyView();
            }
        } else {
            hideLoading();
            Toast.makeText(this, "Fetching from db", Toast.LENGTH_SHORT).show();
            mCustomerInventoryList = CustomerDataUtils.fetchAllCustomers(this);
            if(mCustomerInventoryList.size()>0) {
                hideEmptyView();
                mCustomerInventoryAdapter.updateList(mCustomerInventoryList);
            }else{
                showEmptyView();
            }
        }
    }

    private void init() {
        mCustomerInventoryRecyclerView = (RecyclerView) findViewById(R.id.customer_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCustomerInventoryRecyclerView.setLayoutManager(layoutManager);

        mCustomerInventoryAdapter = new CustomerInventoryAdapter(this, mCustomerInventoryList, this);
        mCustomerInventoryRecyclerView.setAdapter(mCustomerInventoryAdapter);

        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);

        tvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
    }

    @Override
    public void onCustomerSelected(String customerId) {
        if(!NetworkUtils.isNetworkAvailable(this) && OrdersDataUtils.isOrderDBEmptyForCustomer(this, customerId)){
            Toast.makeText(this, R.string.unable_to_reach_server, Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(CustomerListActivity.this, OrderListActivity.class);
            intent.putExtra(Constants.EXTRA_CUSTOMER_ID, customerId);
            startActivity(intent);
        }
    }


    public class FetchCustomerTask extends AsyncTask<Void, Void, Void> {
        private List<CustomerInventory> customerInventoryList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = NetworkUtils.buildURI(CustomerListActivity.this);
            try {
                String customerList = CustomerSyncUtils.fetchAllCustomers(CustomerListActivity.this, url);
                customerInventoryList = CustomerSyncUtils.fetchCustomerList(customerList);
                CustomerDataUtils.insertCustomers(CustomerListActivity.this, customerInventoryList);
                Log.d(LOG_TAG, customerList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCustomerInventoryList = CustomerDataUtils.fetchAllCustomers(CustomerListActivity.this);
            if(mCustomerInventoryList.size()>0) {
                hideEmptyView();
                mCustomerInventoryAdapter.updateList(mCustomerInventoryList);
            }else{
                showEmptyView();
            }
            hideLoading();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showEmptyView() {
        mCustomerInventoryRecyclerView.setVisibility(GONE);
        tvEmptyView.setVisibility(View.VISIBLE);
    }

    private  void hideEmptyView(){
        mCustomerInventoryRecyclerView.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(GONE);
    }


}
