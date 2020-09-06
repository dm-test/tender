package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TendersRepo extends JpaRepository<Tender, Long> {
}
