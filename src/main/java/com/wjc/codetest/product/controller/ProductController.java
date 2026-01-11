package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REVIEW
 * 문제: 동사 중심 URL로 인해 REST API 일관성이 떨어지고 유지보수성이 낮음
 * 원인: 각 API가 /get, /create, /delete, /update 등 행위를 URL에 포함
 *      공통적으로 포함된 /product 경로를 매핑 경로마다 반복
 * 개선안: @RequestMapping("/product") 로 공통 경로 지정
 *      각 메서드의 매핑 경로에서 /product 제거
 * 
 *      동사 형태의 경로 대신 HTTP 메서드로 행위를 표현하여 RESTful API 설계 원칙 준수
 *      ex) /delete/product/{productId} -> DELETE /{productId}
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/get/product/by/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.create(dto);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/delete/product/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/update/product")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductRequest dto){
        Product product = productService.update(dto);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    /**
     * REVIEW
     * 문제: 메소드명이 실제 기능과 불일치하여 가독성 저하
     * 원인: getProductListByCategory 라는 메소드명이 실제 기능과 맞지 않음
     * 개선안: 실제 기능은 Category 목록을 조회하는 것이므로 getCategoryList 와 같은 명확한 메소드명으로 변경이 필요
     */
    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}