package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.product.AddProductRqDto;
import com.github.dmtest.tender.dto.rq.product.RemoveProductRqDto;
import com.github.dmtest.tender.dto.rq.product.UpdateProductRqDto;
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

    @GetMapping("getProductDetails")
    public OperationResultRsDto getProductDetails(@RequestParam("productName") String productName) {
        return productService.getProductDetails(productName);
    }

    @PostMapping("addProduct")
    public OperationResultRsDto addProduct(@RequestBody AddProductRqDto addProductRqDto) {
        return productService.addProduct(addProductRqDto);
    }

    @PutMapping("updateProduct")
    public OperationResultRsDto updateProduct(@RequestBody UpdateProductRqDto updateProductRqDto) {
        return productService.updateProduct(updateProductRqDto);
    }

    @DeleteMapping("removeProduct")
    public OperationResultRsDto removeProduct(@RequestBody RemoveProductRqDto removeProductRqDto) {
        return productService.removeProduct(removeProductRqDto);
    }
}
