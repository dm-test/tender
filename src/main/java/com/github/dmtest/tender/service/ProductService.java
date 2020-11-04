package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ProductsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final ProductsRepo productsRepo;

    @Autowired
    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    public Product getProductByProductName(String productName) {
        return productsRepo.findByProductName(productName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
    }

}
