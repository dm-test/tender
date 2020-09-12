package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "tender_contents")
public class TenderContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contentId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tender_id")
    @JsonBackReference
    private Tender tender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private Integer quantity;
    private BigDecimal costPerUnit;

    protected TenderContent() {
    }

    public TenderContent(Tender tender, Product product, Integer quantity, BigDecimal costPerUnit) {
        this.tender = tender;
        this.product = product;
        this.quantity = quantity;
        this.costPerUnit = costPerUnit;
    }
}
