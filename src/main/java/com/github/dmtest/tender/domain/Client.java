package com.github.dmtest.tender.domain;

import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private String clientName;

    @Getter
    @Setter
    private String clientAddress;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Contract> contracts = new ArrayList<>();

    protected Client() {
    }

    public Client(String clientName, String clientAddress) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
    }

    public List<Contract> getContracts() {
        return Collections.unmodifiableList(contracts);
    }

    public Optional<Contract> getContract(String tenderNumber) {
        return contracts.stream().filter(contract -> contract.getContractNumber().equals(tenderNumber)).findFirst();
    }

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }

}
