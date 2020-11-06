package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString(of = "productName")
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID productId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TenderItem> items = new ArrayList<>();

    @Getter
    @Setter
    private String productName;

    @Getter
    @Setter
    private String manufacturer;

    @Getter
    @Setter
    private String country;

    protected Product() {
    }

    public Product(String productName, String manufacturer, String country) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.country = country;
    }

}
