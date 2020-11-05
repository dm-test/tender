package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.product.AddProductRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("getProducts")
    public OperationResultRsDto getProducts() {
        return productService.getProducts();
    }

    @PostMapping("addProduct")
    public OperationResultRsDto addProduct(@RequestBody AddProductRqDto addProductRqDto) {
        return productService.addProduct(addProductRqDto);
    }
}
