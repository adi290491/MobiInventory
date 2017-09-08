package com.example.android.mobiinventory.customers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mobiinventory.R;

import java.util.List;

/**
 * Created by aditya.sawant on 06-09-2017.
 */

public class CustomerInventoryAdapter extends RecyclerView.Adapter<CustomerInventoryAdapter.CustomerViewHolder> {
    private Context mContext;
    private List<CustomerInventory> customerInventoryList;
    private CustomerSelectListener mCustomerSelectListener;

    public CustomerInventoryAdapter(Context context, List<CustomerInventory> customerInventoryList, CustomerSelectListener customerSelectListener){
        this.mContext = context;
        this.customerInventoryList = customerInventoryList;
        this.mCustomerSelectListener = customerSelectListener;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.customer_inventory_list_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        CustomerInventory customerInventory = customerInventoryList.get(position);
        holder.customerNameTextView.setText(customerInventory.getCustomerName());
        holder.companyNameTextView.setText(customerInventory.getCompanyName());
        holder.countryPostalTextView.setText(customerInventory.getCountry() + " - " + customerInventory.getPostalCode());
        holder.contactNumberTextView.setText(customerInventory.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return customerInventoryList.size();
    }

    public void updateList(List<CustomerInventory> customerInventoryList) {
        this.customerInventoryList = customerInventoryList;
        notifyDataSetChanged();
    }


    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView customerNameTextView, companyNameTextView, countryPostalTextView, contactNumberTextView;
        public CustomerViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = (TextView)itemView.findViewById(R.id.tv_customer_name);
            companyNameTextView = (TextView)itemView.findViewById(R.id.tv_company_name);
            countryPostalTextView = (TextView)itemView.findViewById(R.id.tv_country_postal_code);
            contactNumberTextView = (TextView)itemView.findViewById(R.id.tv_phone_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            CustomerInventory customerInventory = customerInventoryList.get(position);
            mCustomerSelectListener.onCustomerSelected(customerInventory.getCustomerId());
        }
    }

    public interface CustomerSelectListener{
        public void onCustomerSelected(String customerId);
    }
}
