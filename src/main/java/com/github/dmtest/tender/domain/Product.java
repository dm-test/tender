package com.github.dmtest.tender.domain;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ToString(of = {"name", "manufacturer", "country"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String manufacturer;
    private String country;

    protected Product() {
    }

    public Product(String name, String manufacturer, String country) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getCountry() {
        return country;
    }
}
