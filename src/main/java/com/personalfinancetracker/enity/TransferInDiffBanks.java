package com.personalfinancetracker.enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table(name = "MultiBankTransaction")
public class TransferInDiffBanks {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long multiBankId;
	
	private String senderBankAc;
	
	private String receiverBankAc;
	
	private String transactionTime;
	
	private String description;
	
	@Column(name = "Amount")
	private Double multitransactionAmount;
	
	
	private String status;
	
	
	
	
	

}
