package com.coding.practice.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


import lombok.Data;

@Entity
@Data
public class Approval {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Product product;

	private LocalDateTime requestDate;
	private ApprovalStatus status;

	// Getters and setters
}
