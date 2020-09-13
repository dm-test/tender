package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientsRepo extends JpaRepository<Client, UUID> {
}
