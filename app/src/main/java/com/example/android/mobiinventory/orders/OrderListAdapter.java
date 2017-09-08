package com.example.android.mobiinventory.orders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mobiinventory.R;

import java.util.List;

/**
 * Created by aditya.sawant on 08-09-2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private Context mContext;
    private List<Order> orderList;

    public OrderListAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText(String.valueOf(order.getOrderId()));
        holder.requiredDateTextView.setText(order.getRequiredDate());
        holder.shipAddressTextView.setText(order.getShipAddress());
        holder.shipCountryTextView.setText(order.getShipCountry());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView orderIdTextView, requiredDateTextView, shipAddressTextView, shipCountryTextView;
        public OrderViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = (TextView)itemView.findViewById(R.id.tv_order_id);
            requiredDateTextView = (TextView)itemView.findViewById(R.id.tv_required_date);
            shipAddressTextView = (TextView)itemView.findViewById(R.id.tv_ship_address);
            shipCountryTextView = (TextView)itemView.findViewById(R.id.tv_ship_country);
        }
    }

    public void updateOrderList(List<Order> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    }
}
