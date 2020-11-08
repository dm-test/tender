package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@ToString(of = "contractNumber")
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID contractId;

    @Getter
    @Setter
    private String contractNumber;

    @Getter
    @Setter
    private LocalDate contractDate;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ContractItem> items = new ArrayList<>();

    protected Contract() {
    }

    public Contract(String contractNumber, LocalDate contractDate, Client client) {
        this.contractNumber = contractNumber;
        this.contractDate = contractDate;
        this.client = client;
    }

    public List<ContractItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Optional<ContractItem> getItem(String productName) {
        return items.stream().filter(item -> item.getProduct().getProductName().equals(productName)).findFirst();
    }

    public void addItem(ContractItem item) {
        items.add(item);
    }

    public void removeItem(ContractItem item) {
        items.remove(item);
    }
}
