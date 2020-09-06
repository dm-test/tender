package com.github.dmtest.tender.repo;

import com.github.dmtest.tender.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepo extends JpaRepository<Product, Long> {
}
