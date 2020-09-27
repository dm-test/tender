package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID productId;

    private String productName;
    private String manufacturer;
    private String country;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<TenderItem> tenderContents;

    protected Product() {
    }

    public Product(String productName, String manufacturer, String country) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.country = country;
    }

}
