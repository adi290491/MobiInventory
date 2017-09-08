package com.example.android.mobiinventory.customers;

import static android.R.attr.id;

/**
 * Created by aditya.sawant on 06-09-2017.
 */

public class CustomerInventory {
    private String customerId;
    private String customerName;
    private String companyName;
    private String country;
    private String postalCode;
    private String phoneNumber;

    public CustomerInventory() {
    }

    public CustomerInventory(String customerId, String customerName, String companyName, String country, String postalCode, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.companyName = companyName;
        this.country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
