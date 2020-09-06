package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;

import javax.persistence.*;

@ToString(of = {"tenderNumber"})
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tenderId;

    private String tenderNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    protected Tender() {
    }

    public Tender(String tenderNumber, Client client) {
        this.tenderNumber = tenderNumber;
        this.client = client;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public String getTenderNumber() {
        return tenderNumber;
    }

    public Client getClient() {
        return client;
    }
}
