package com.example.android.mobiinventory.orders;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

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

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView mOrderListRecyclerView;
    private OrderListAdapter mOrderListAdapter;
    private List<Order> mOrdersList = new ArrayList<>();
    private String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        init();
        customerId = getIntent().getStringExtra(Constants.EXTRA_CUSTOMER_ID);
        if(!TextUtils.isEmpty(customerId)){
            if(OrdersDataUtils.isOrderDBEmptyForCustomer(this, customerId)){
                new OrderFetchTask().execute();
            }else{
                mOrdersList = OrdersDataUtils.fetchAllOrders(this, customerId);
                mOrderListAdapter.updateOrderList(mOrdersList);
            }
        }
    }

    private void init() {
        mOrderListRecyclerView = (RecyclerView) findViewById(R.id.order_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false);
        mOrderListRecyclerView.setLayoutManager(layoutManager);
        mOrderListAdapter = new OrderListAdapter(this, mOrdersList);
        mOrderListRecyclerView.setAdapter(mOrderListAdapter);
    }

    public class OrderFetchTask extends AsyncTask<Void, Void, Void>{
        private List<Order> orderList = new ArrayList<>();
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
            mOrderListAdapter.updateOrderList(mOrdersList);
        }
    }
}
