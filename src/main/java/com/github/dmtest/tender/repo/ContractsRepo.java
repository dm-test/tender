package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContractsRepo extends JpaRepository<Contract, UUID> {
    Optional<Contract> findByContractNumber(String contractNumber);
}
