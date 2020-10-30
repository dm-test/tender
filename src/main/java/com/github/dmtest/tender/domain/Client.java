package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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
    private Set<Tender> tenders;

    protected Client() {
    }

    public Client(String clientName) {
        this.clientName = clientName;
        tenders = new HashSet<>();
    }

    public void addTender(Tender tender) {
        tender.setClient(this);
        tenders.add(tender);
    }

}
