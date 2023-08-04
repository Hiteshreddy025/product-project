package com.coding.practice.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coding.practice.demo.entity.Approval;
import com.coding.practice.demo.entity.Product;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findAllByOrderByRequestDateAsc();
    Approval findByProduct(Product product);
}