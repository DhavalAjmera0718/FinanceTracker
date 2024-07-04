package com.personalfinancetracker.enity;

import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String accountNo;
	private String accountType;
	@Min(1000)
	private Double balance;
	
	private String adharNumber;
	
	private String matchedId;
	
	private String bank;
//	
//	@OneToMany(targetEntity = TransactionEntity.class)
//	@Cascade(CascadeType.ALL)
//	@JoinColumn(name = "BankID" , referencedColumnName = "id")
//	private List<TransactionEntity> transactionEntities;
	
}
