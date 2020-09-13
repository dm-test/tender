package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.TenderContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenderContentsRepo extends JpaRepository<TenderContent, UUID> {
}
