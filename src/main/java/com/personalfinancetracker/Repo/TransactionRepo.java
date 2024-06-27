package com.personalfinancetracker.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personalfinancetracker.enity.TransactionEntity;
import java.util.List;


public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
	
	
	List<TransactionEntity> findByWalletId(Long walletId);
	

}
