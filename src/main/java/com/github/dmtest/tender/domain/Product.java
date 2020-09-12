package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String manufacturer;
    private String country;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TenderContent> tenderContents;

    protected Product() {
    }

    public Product(String productName, String manufacturer, String country) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.country = country;
    }

}
