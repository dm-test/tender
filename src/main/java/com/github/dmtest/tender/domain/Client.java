package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID clientId;

    private String clientName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private final List<Tender> tenders = new ArrayList<>();

    protected Client() {
    }

    public Client(String clientName) {
        this.clientName = clientName;
    }

    public void addTender(Tender tender) {
        tenders.add(tender);
    }

    public Optional<Tender> getTender(String tenderNumber) {
        return tenders.stream().filter(tender -> tender.getTenderNumber().equals(tenderNumber)).findFirst();
    }

    public boolean removeTender(String tenderNumber) {
        return tenders.removeIf(tender -> tender.getTenderNumber().equals(tenderNumber));
    }

}
