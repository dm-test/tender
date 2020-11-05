package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@ToString(of = "product")
@Entity
@Table(name = "tender_items")
public class TenderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID itemId;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id")
    private Tender tender;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Getter
    @Setter
    private Integer quantity;

    @Getter
    @Setter
    private BigDecimal costPerUnit;

    protected TenderItem() {
    }

    public TenderItem(Product product, Integer quantity, BigDecimal costPerUnit, Tender tender) {
        this.product = product;
        this.quantity = quantity;
        this.costPerUnit = costPerUnit;
        this.tender = tender;
    }
}
