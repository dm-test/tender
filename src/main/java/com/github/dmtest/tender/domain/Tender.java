package com.github.dmtest.tender.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID tenderId;

    private String tenderNumber;

    private LocalDate tenderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
//    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<TenderItem> items = new HashSet<>();

    protected Tender() {
    }

    public Tender(String tenderNumber, LocalDate tenderDate) {
        this.tenderNumber = tenderNumber;
        this.tenderDate = tenderDate;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void addItem(TenderItem item) {
        items.add(item);
    }

}
