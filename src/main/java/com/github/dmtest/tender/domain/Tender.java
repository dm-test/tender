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
import java.util.UUID;

@Getter
@ToString
@Entity
@Table(name = "tenders")
public class Tender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    private UUID tenderId;

    private String tenderNumber;

    @Setter
    private LocalDate tenderDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
//    @JsonBackReference
    private Client client;

    @ToString.Exclude
    @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<TenderItem> items = new ArrayList<>();

    protected Tender() {
    }

    public Tender(String tenderNumber, LocalDate tenderDate) {
        this.tenderNumber = tenderNumber;
        this.tenderDate = tenderDate;
    }

    public void addItem(TenderItem item) {
        item.setTender(this);
        items.add(item);
    }

}
