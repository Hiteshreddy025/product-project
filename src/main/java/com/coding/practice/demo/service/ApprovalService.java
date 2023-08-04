package com.coding.practice.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.practice.demo.entity.Approval;
import com.coding.practice.demo.entity.Product;
import com.coding.practice.demo.exception.NotFoundException;
import com.coding.practice.demo.repository.ApprovalRepository;

@Service
public class ApprovalService {
	// Implement methods for approval and rejection

	@Autowired
	private ApprovalRepository approvalRepository;

	@Autowired
	private ProductService productService;

	public void approveProduct(Long approvalId) {
		Approval approval = approvalRepository.findById(approvalId)
				.orElseThrow(() -> new NotFoundException("Approval not found with id: " + approvalId));

		Product product = approval.getProduct();
		product.setStatus(true); // Approve the product
		productService.updateProductStatus(product.getId(), true);

		approvalRepository.delete(approval); // Remove from approval queue
	}

	public void rejectProduct(Long approvalId) {
		Approval approval = approvalRepository.findById(approvalId)
				.orElseThrow(() -> new NotFoundException("Approval not found with id: " + approvalId));

		approvalRepository.delete(approval); // Remove from approval queue
	}

	public List<Approval> getApprovalQueue() {
		return approvalRepository.findAllByOrderByRequestDateAsc();
	}

}
