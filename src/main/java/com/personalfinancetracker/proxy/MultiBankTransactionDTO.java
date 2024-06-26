package com.personalfinancetracker.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultiBankTransactionDTO {
	
	private String receiverBankAc;
	private String senderBankAc;
	private Double sendMoney;
	private Double receiveMoney;
	private String trTime;
	private String description;

}
