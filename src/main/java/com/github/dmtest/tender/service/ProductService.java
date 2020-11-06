package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.dto.rq.product.AddProductRqDto;
import com.github.dmtest.tender.dto.rq.product.RemoveProductRqDto;
import com.github.dmtest.tender.dto.rq.product.UpdateProductRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.product.GetProductDetailsRsDto;
import com.github.dmtest.tender.dto.rs.body.product.GetProductRsDto;
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
        List<GetProductRsDto> getProductRsDtoList = productsRepo.findAll().stream()
                .map(product -> new GetProductRsDto(product.getProductName()))
                .collect(Collectors.toList());
        LOG.info("Получен список продуктов");
        return new OperationResultRsDto(OperationResult.SUCCESS, getProductRsDtoList);
    }

    public OperationResultRsDto getProductDetails(String productName) {
        Product product = getProductByProductName(productName);
        String manufacturer = product.getManufacturer();
        String country = product.getCountry();
        GetProductDetailsRsDto getProductDetailsRsDto = new GetProductDetailsRsDto(productName, manufacturer, country);
        LOG.info("Получена информация по продукту '{}'", product);
        return new OperationResultRsDto(OperationResult.SUCCESS, getProductDetailsRsDto);
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

    public OperationResultRsDto updateProduct(UpdateProductRqDto updateProductRqDto) {
        String productName = updateProductRqDto.getProductName();
        Product product = getProductByProductName(productName);
        String manufacturer = product.getManufacturer();
        String country = product.getCountry();
        String productNameNew = updateProductRqDto.getUpdatableData().getProductNameNew();
        String manufacturerNew = updateProductRqDto.getUpdatableData().getManufacturerNew();
        String countryNew = updateProductRqDto.getUpdatableData().getCountryNew();
        product.setProductName(productNameNew);
        product.setManufacturer(manufacturerNew);
        product.setCountry(countryNew);
        productsRepo.save(product);
        LOG.info("Продукт с именем '{}' обновлен. Имя продукта: '{}' -> '{}', Производитель: '{}' -> '{}', Страна: '{}' -> '{}'",
                productName, productName, productNameNew, manufacturer, manufacturerNew, country, countryNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Продукт успешно обновлен");
    }

    public OperationResultRsDto removeProduct(RemoveProductRqDto removeProductRqDto) {
        String productName = removeProductRqDto.getProductName();
        Product product = getProductByProductName(productName);
        productsRepo.delete(product);
        LOG.info("Продукт '{}' удален", product);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Продукт '%s' успешно удален", productName));
    }

    public Product getProductByProductName(String productName) {
        return productsRepo.findByProductName(productName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
    }

}
