package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@ToString(of = "tenderNumber")
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID tenderId;

    @Getter
    @Setter
    private String tenderNumber;

    @Getter
    @Setter
    private LocalDate tenderDate;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TenderItem> items = new ArrayList<>();

    protected Tender() {
    }

    public Tender(String tenderNumber, LocalDate tenderDate, Client client) {
        this.tenderNumber = tenderNumber;
        this.tenderDate = tenderDate;
        this.client = client;
    }

    public List<TenderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Optional<TenderItem> getItem(String productName) {
        return items.stream().filter(item -> item.getProduct().getProductName().equals(productName)).findFirst();
    }

    public void addItem(TenderItem item) {
        items.add(item);
    }

    public void removeItem(TenderItem item) {
        items.remove(item);
    }
}
