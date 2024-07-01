package com.personalfinancetracker.proxy;

import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletProxy {

	private Long id;
	private String name;
	private String accountNo;
	private String accountType;
	private Double balance;
	@Min(12)
	@Max(12)
	private String adharNumber;
}
