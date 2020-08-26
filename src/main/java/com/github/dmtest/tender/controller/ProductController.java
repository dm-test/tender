package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductRepo productRepo;

    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("getProducts")
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @PostMapping("addProduct")
    public Product createClient(@RequestBody Product product) {
        return productRepo.save(product);
    }
}
