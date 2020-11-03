package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@ToString
@Getter
@Entity
@Table(name = "tender_items")
public class TenderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID itemId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id")
//    @JsonBackReference
    private Tender tender;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
//    @JsonBackReference
    private Product product;

    @Setter
    private Integer quantity;

    @Setter
    private BigDecimal costPerUnit;

    protected TenderItem() {
    }

    public TenderItem(Product product, Integer quantity, BigDecimal costPerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.costPerUnit = costPerUnit;
    }
}
