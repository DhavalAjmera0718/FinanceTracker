package com.personalfinancetracker.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

	private Long tRid;
	private String tRname;

	private String tRaccountNo; // Reciver Account
	private String tRaccountType;
	private Double tRbalance;
	private String tRsenderAccountNo;

	private String tRdescription;

	private String tRstatus;

	private String transactionTime;

	private Long walletId;
	private Double bankDeposite;

	private Double bankWithDraw;

}
