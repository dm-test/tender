package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.TenderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenderItemsRepo extends JpaRepository<TenderItem, UUID> {
}
