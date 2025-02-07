package com.eventsourcing.payment.query.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private String itemId;
    private double price;
    private String description;

    protected Product() {}

    public Product(String itemId, double price, String description) {
        this.itemId = itemId;
        this.price = price;
        this.description = description;
    }

    public String getItemId() {
        return itemId;
    }

    public double getPrice() {
        return price;
    }
}