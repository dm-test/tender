package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.dto.rq.ProductRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.ProductRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.repo.ProductsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
    private final ProductsRepo productsRepo;

    @Autowired
    public ProductController(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    @GetMapping("getProducts")
    public OperationResultRsDto getProducts() {
        List<ProductRsDto> products = productsRepo.findAll().stream()
                .map(pr -> new ProductRsDto(pr.getProductName(), pr.getManufacturer(), pr.getCountry()))
                .collect(Collectors.toList());
        LOG.info("Получен список продуктов");
        return new OperationResultRsDto(OperationResult.SUCCESS, products);
    }

    @PostMapping("addProduct")
    public OperationResultRsDto addProduct(@RequestBody ProductRqDto productRqDto) {
        String productName = productRqDto.getProductName();
        String manufacturer = productRqDto.getManufacturer();
        String country = productRqDto.getCountry();
        Product product = new Product(productName, manufacturer, country);
        productsRepo.save(product);
        String msg = String.format("Продукт с именем '%s' добавлен", productName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }
}
