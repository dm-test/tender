package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepo extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductName(String productName);
}
