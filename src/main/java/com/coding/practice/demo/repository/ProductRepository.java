package com.coding.practice.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coding.practice.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusOrderByPostedDateDesc(boolean status);
    List<Product> findByStatusAndNameContainingAndPriceBetweenAndPostedDateBetweenOrderByPostedDateDesc(
            boolean status, String productName, BigDecimal minPrice, BigDecimal maxPrice,
            LocalDateTime minPostedDate, LocalDateTime maxPostedDate
        );
    
    @Query("SELECT p FROM Product p WHERE " +
    	       "(:productName IS NULL OR p.name LIKE %:productName%) " +
    	       "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
    	       "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
    	       "AND (:minPostedDate IS NULL OR p.postedDate >= :minPostedDate) " +
    	       "AND (:maxPostedDate IS NULL OR p.postedDate <= :maxPostedDate)")
    	List<Product> searchProducts(@Param("productName") String productName,
    	                             @Param("minPrice") BigDecimal minPrice,
    	                             @Param("maxPrice") BigDecimal maxPrice,
    	                             @Param("minPostedDate") LocalDate minPostedDate,
    	                             @Param("maxPostedDate") LocalDate maxPostedDate);

}
