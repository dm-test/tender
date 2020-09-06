package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tenderId;

    private String tenderNumber;

    private LocalDate tenderDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    protected Tender() {
    }

    public Tender(String tenderNumber, LocalDate tenderDate, Client client) {
        this.tenderNumber = tenderNumber;
        this.tenderDate = tenderDate;
        this.client = client;
    }
}
