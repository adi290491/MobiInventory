package com.example.android.mobiinventory.orders;

/**
 * Created by aditya.sawant on 08-09-2017.
 */

public class Order {
    private int orderId;
    private String customerId;
    private String requiredDate;
    private String shipAddress;
    private String shipCountry;

    public Order() {
    }

    public Order(int orderId, String customerId, String requiredDate, String shipAddress, String shipCountry) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.requiredDate = requiredDate;
        this.shipAddress = shipAddress;
        this.shipCountry = shipCountry;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }
}
