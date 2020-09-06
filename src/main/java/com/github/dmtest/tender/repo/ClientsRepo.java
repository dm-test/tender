package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepo extends JpaRepository<Client, Long> {
}
