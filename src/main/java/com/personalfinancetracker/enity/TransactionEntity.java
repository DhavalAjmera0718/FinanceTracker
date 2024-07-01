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
@Getter
@Setter
@Data
@Table(name = "transactionTable")
public class TransactionEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tRid;
	private String tRname;
	@Column(name = "ReceiverAccountNo")
	private String tRaccountNo; // Reciver Account
	private String tRaccountType;
	private Double tRbalance;
	private String tRsenderAccountNo;
	
	@Column(name = "Description")
	private String tRdescription;
	
	private String tRstatus;
	
	private String transactionTime;
	
	 @Column(name = "BankID1")
	  private Long walletId;
	


}
