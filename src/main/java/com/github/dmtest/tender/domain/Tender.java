package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

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

    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TenderContent> tenderContents;

    protected Tender() {
    }

    public Tender(String tenderNumber, LocalDate tenderDate, Client client) {
        this.tenderNumber = tenderNumber;
        this.tenderDate = tenderDate;
        this.client = client;
    }
}
