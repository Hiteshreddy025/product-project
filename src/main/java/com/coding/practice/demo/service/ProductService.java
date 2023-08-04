package com.coding.practice.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.practice.demo.entity.Approval;
import com.coding.practice.demo.entity.ApprovalStatus;
import com.coding.practice.demo.entity.Product;
import com.coding.practice.demo.exception.NotFoundException;
import com.coding.practice.demo.exception.ValidationException;
import com.coding.practice.demo.repository.ApprovalRepository;
import com.coding.practice.demo.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ApprovalRepository approvalRepository;

	public List<Product> getProducts() {
		return productRepository.findByStatusOrderByPostedDateDesc(true);
	}

	public void deleteProduct(Long id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			throw new RuntimeException("product id not find");
		}
		productRepository.delete(productOpt.get());
	}

	public List<Product> searchProducts(String productName, BigDecimal minPrice, BigDecimal maxPrice,
			LocalDate minPostedDate, LocalDate maxPostedDate) {
		boolean status = true; // Assuming we are searching for active products

		return productRepository.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
	}

	public Product saveProduct(Product product) {
	        if (product.getPrice().compareTo(BigDecimal.valueOf(10000)) > 0) {
	            throw new ValidationException("Product price cannot exceed $10,000.");
	        }

	        // Validate price and set status based on the price
	        boolean requiresApproval = false;
	        if (product.getPrice().compareTo(BigDecimal.valueOf(5000)) > 0) {
	            requiresApproval = true;
	        }

	        product.setStatus(!requiresApproval);

	        Product createdProduct = productRepository.save(product);

	        if (requiresApproval) {
	            pushToApprovalQueue(createdProduct);
	        }

	        return createdProduct;
	    }

	    private void pushToApprovalQueue(Product product) {
	        Approval approval = new Approval();
	        approval.setProduct(product);
	        approval.setRequestDate(LocalDateTime.now());
	        approval.setStatus(ApprovalStatus.PENDING);
	        approvalRepository.save(approval);
	    }

	public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        // Check if the updated price requires approval
        boolean requiresApproval = false;
        if (updatedProduct.getPrice() != null && updatedProduct.getPrice().compareTo(existingProduct.getPrice().multiply(BigDecimal.valueOf(1.5))) > 0) {
            requiresApproval = true;
        }

		if(updatedProduct.getName() != null)
	        existingProduct.setName(updatedProduct.getName());
		if(updatedProduct.getPrice() != null)
	        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStatus(!requiresApproval);

        Product updatedProductEntity = productRepository.save(existingProduct);

        if (requiresApproval) {
            pushToApprovalQueue(updatedProductEntity);
        }

        return updatedProductEntity;
    }


	public void updateProductStatus(Long id, boolean b) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
		existingProduct.setStatus(b);
		productRepository.save(existingProduct);

	}

}
