package com.personalfinancetracker.enity;

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
	private String tRaccountNo;
	private String tRaccountType;
	private Double tRbalance;


}
