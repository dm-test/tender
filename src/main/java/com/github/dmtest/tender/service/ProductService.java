package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.dto.rq.product.AddProductRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.ProductRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ProductsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private final ProductsRepo productsRepo;

    @Autowired
    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    public OperationResultRsDto getProducts() {
        List<ProductRsDto> products = productsRepo.findAll().stream()
                .map(pr -> new ProductRsDto(pr.getProductName(), pr.getManufacturer(), pr.getCountry()))
                .collect(Collectors.toList());
        LOG.info("Получен список продуктов");
        return new OperationResultRsDto(OperationResult.SUCCESS, products);
    }

    public OperationResultRsDto addProduct(AddProductRqDto addProductRqDto) {
        String productName = addProductRqDto.getProductName();
        String manufacturer = addProductRqDto.getManufacturer();
        String country = addProductRqDto.getCountry();
        Product product = new Product(productName, manufacturer, country);
        productsRepo.save(product);
        String msg = String.format("Продукт с именем '%s' добавлен", productName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public Product getProductByProductName(String productName) {
        return productsRepo.findByProductName(productName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
    }

}
