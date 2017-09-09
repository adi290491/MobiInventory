package com.example.android.mobiinventory.orders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mobiinventory.R;
import com.example.android.mobiinventory.orders.data.OrdersDataUtils;
import com.example.android.mobiinventory.utils.Constants;
import com.example.android.mobiinventory.utils.CustomerSyncUtils;
import com.example.android.mobiinventory.utils.NetworkUtils;
import com.example.android.mobiinventory.utils.OrderSyncUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView mOrderListRecyclerView;
    private OrderListAdapter mOrderListAdapter;
    private TextView tvEmptyView;
    private List<Order> mOrdersList = new ArrayList<>();
    private String customerId;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        init();
        customerId = getIntent().getStringExtra(Constants.EXTRA_CUSTOMER_ID);
        if (!TextUtils.isEmpty(customerId)) {
            if (OrdersDataUtils.isOrderDBEmptyForCustomer(this, customerId)) {
                if(NetworkUtils.isNetworkAvailable(this)) {
                    new OrderFetchTask().execute();
                }else{
                    showEmptyView();
                    hideLoading();
                    Toast.makeText(this, R.string.unable_to_reach_server, Toast.LENGTH_SHORT).show();
                }
            } else {
                hideLoading();
                mOrdersList = OrdersDataUtils.fetchAllOrders(this, customerId);
                if (mOrdersList.size() > 0) {
                    hideEmptyView();
                    mOrderListAdapter.updateOrderList(mOrdersList);
                } else {
                    showEmptyView();
                }
            }
        }
    }

    private void init() {
        mOrderListRecyclerView = (RecyclerView) findViewById(R.id.order_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mOrderListRecyclerView.setLayoutManager(layoutManager);
        mOrderListAdapter = new OrderListAdapter(this, mOrdersList);
        mOrderListRecyclerView.setAdapter(mOrderListAdapter);
        tvEmptyView = (TextView) findViewById(R.id.tv_order_empty_view);
        loadingProgressBar = (ProgressBar) findViewById(R.id.order_loading_progress_bar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction(CONNECTIVITY_SERVICE);
        registerReceiver(connectivityReceiver, internetFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    public class OrderFetchTask extends AsyncTask<Void, Void, Void> {
        private List<Order> orderList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = NetworkUtils.buildURI(OrderListActivity.this, customerId);
            try {
                String orderListString = CustomerSyncUtils.fetchAllCustomers(OrderListActivity.this, url);
                orderList = OrderSyncUtils.fetchOrderList(orderListString);
                OrdersDataUtils.insertOrders(OrderListActivity.this, orderList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mOrdersList = OrdersDataUtils.fetchAllOrders(OrderListActivity.this, customerId);
            if (mOrdersList.size() > 0) {
                hideEmptyView();
                mOrderListAdapter.updateOrderList(mOrdersList);
            } else {
                showEmptyView();
            }
            hideLoading();
        }
    }

    private void showEmptyView() {
        mOrderListRecyclerView.setVisibility(GONE);
        tvEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mOrderListRecyclerView.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);
    }

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                /*final SnackbarWrapper snackbarWrapper = SnackbarWrapper.make(getApplicationContext(),
                        "Test snackbarWrapper", Snackbar.LENGTH_SHORT);
                snackbarWrapper.show();*/
                Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_order_list), "No Connection", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };
    private void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(GONE);
    }

}
