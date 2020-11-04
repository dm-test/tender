package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@ToString(of = "tenderNumber")
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID tenderId;

    @Setter
    private String tenderNumber;

    @Setter
    private LocalDate tenderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
//    @JsonBackReference
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

    public void addItem(TenderItem item) {
        items.add(item);
    }

    public Optional<TenderItem> getItem(String productName) {
        return items.stream().filter(item -> item.getProduct().getProductName().equals(productName)).findFirst();
    }

    public boolean removeItem(String productName) {
        return items.removeIf(item -> item.getProduct().getProductName().equals(productName));
    }

}
