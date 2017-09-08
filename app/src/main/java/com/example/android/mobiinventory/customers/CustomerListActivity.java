package com.example.android.mobiinventory.customers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.mobiinventory.R;
import com.example.android.mobiinventory.customers.data.CustomerDataUtils;
import com.example.android.mobiinventory.orders.OrderListActivity;
import com.example.android.mobiinventory.utils.Constants;
import com.example.android.mobiinventory.utils.CustomerSyncUtils;
import com.example.android.mobiinventory.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class CustomerListActivity extends AppCompatActivity implements CustomerInventoryAdapter.CustomerSelectListener {

    private static final String LOG_TAG = CustomerListActivity.class.getSimpleName();
    private RecyclerView mCustomerInventoryRecyclerView;
    private CustomerInventoryAdapter mCustomerInventoryAdapter;
    private List<CustomerInventory> mCustomerInventoryList = new ArrayList<>();
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if (CustomerDataUtils.isCustomerDBEmpty(this)) {
            if(NetworkUtils.isNetworkAvailable(this)) {
                Toast.makeText(this, "Fetching from network", Toast.LENGTH_SHORT).show();
                new FetchCustomerTask().execute();
            }else{
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), "No Connection", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else {
            hideLoading();
            Toast.makeText(this, "Fetching from db", Toast.LENGTH_SHORT).show();
            mCustomerInventoryList = CustomerDataUtils.fetchAllCustomers(this);
            mCustomerInventoryAdapter.updateList(mCustomerInventoryList);

        }
    }

    private void init() {
        mCustomerInventoryRecyclerView = (RecyclerView) findViewById(R.id.customer_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCustomerInventoryRecyclerView.setLayoutManager(layoutManager);

        mCustomerInventoryAdapter = new CustomerInventoryAdapter(this, mCustomerInventoryList, this);
        mCustomerInventoryRecyclerView.setAdapter(mCustomerInventoryAdapter);

        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
    }

    @Override
    public void onCustomerSelected(String customerId) {
        Intent intent = new Intent(CustomerListActivity.this, OrderListActivity.class);
        intent.putExtra(Constants.EXTRA_CUSTOMER_ID, customerId);
        startActivity(intent);
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
            mCustomerInventoryAdapter.updateList(mCustomerInventoryList);
            hideLoading();
        }
    }

    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(GONE);
    }
}
