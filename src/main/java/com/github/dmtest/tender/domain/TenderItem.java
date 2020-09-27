package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "tender_items")
public class TenderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id")
    @JsonBackReference
    private Tender tender;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private Integer quantity;
    private BigDecimal costPerUnit;

    protected TenderItem() {
    }

    public TenderItem(Product product, Integer quantity, BigDecimal costPerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.costPerUnit = costPerUnit;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }
}
