package com.personalfinancetracker.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personalfinancetracker.enity.Wallet;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long>{
	
	
	Optional<Wallet> findByAccountNo(String accountNo);
	
	Optional<Wallet> findByAdharNumber(String adharNumber);

}
