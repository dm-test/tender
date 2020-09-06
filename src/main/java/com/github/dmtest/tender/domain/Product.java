package com.github.dmtest.tender.domain;

import lombok.ToString;

import javax.persistence.*;

@ToString(of = {"productName", "manufacturer", "country"})
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String manufacturer;
    private String country;

    protected Product() {
    }

    public Product(String productName, String manufacturer, String country) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.country = country;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCountry() {
        return country;
    }
}
