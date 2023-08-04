package com.coding.practice.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coding.practice.demo.entity.Approval;
import com.coding.practice.demo.service.ApprovalService;

@RestController
@RequestMapping("/api/products/approval-queue")
public class ApprovalController {
	// Implement methods for getting approval queue, approving, and rejecting
	@Autowired
	private ApprovalService approvalService;

	@GetMapping
	public ResponseEntity<List<Approval>> getApprovalQueue() {
		List<Approval> approvalQueue = approvalService.getApprovalQueue();
		return new ResponseEntity<>(approvalQueue, HttpStatus.OK);
	}

	@PutMapping("/{approvalId}/approve")
	public ResponseEntity<Void> approveProduct(@PathVariable Long approvalId) {
		approvalService.approveProduct(approvalId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{approvalId}/reject")
	public ResponseEntity<Void> rejectProduct(@PathVariable Long approvalId) {
		approvalService.rejectProduct(approvalId);
		return ResponseEntity.noContent().build();
	}

}