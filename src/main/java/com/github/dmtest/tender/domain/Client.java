package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@ToString(of = "clientName")
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID clientId;

    @Getter
    private String clientName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Tender> tenders = new ArrayList<>();

    protected Client() {
    }

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public List<Tender> getTenders() {
        return Collections.unmodifiableList(tenders);
    }

    public Optional<Tender> getTender(String tenderNumber) {
        return tenders.stream().filter(tender -> tender.getTenderNumber().equals(tenderNumber)).findFirst();
    }

    public void addTender(Tender tender) {
        tenders.add(tender);
    }

    public void removeTender(Tender tender) {
        tenders.remove(tender);
    }

}
