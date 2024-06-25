package com.personalfinancetracker.proxy;

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
}
