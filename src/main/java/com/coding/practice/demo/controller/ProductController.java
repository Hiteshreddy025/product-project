package com.coding.practice.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coding.practice.demo.entity.Product;
import com.coding.practice.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Implement methods for each API endpoint
	
	
	@Autowired
	ProductService productService;
	//sorty by created date --latest first
	@GetMapping
	public List<Product> getProducts(){
		List<Product> allProducts = productService.getProducts();
		return allProducts;
	}
	
	 @GetMapping("/search")
	    public ResponseEntity<List<Product>> searchProducts(
	            @RequestParam(required = false) String productName,
	            @RequestParam(required = false) BigDecimal minPrice,
	            @RequestParam(required = false) BigDecimal maxPrice,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate minPostedDate,
	            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate maxPostedDate
	    ) {
	        List<Product> products = productService.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
	        return new ResponseEntity<>(products, HttpStatus.OK);
	    }
	
	  @PostMapping
	    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
	        Product createdProduct = productService.saveProduct(product);
	        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	    }
	
	 @PutMapping("/{productId}")
	    public ResponseEntity<Product> updateProduct(
	            @PathVariable Long productId, @RequestBody Product product) {
	        Product updatedProduct = productService.updateProduct(productId, product);
	        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
	    }

	
	  @DeleteMapping("/{productId}")
	    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
	        productService.deleteProduct(productId);
	        return ResponseEntity.noContent().build();
	    }
}

