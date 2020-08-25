package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Long> {
}
