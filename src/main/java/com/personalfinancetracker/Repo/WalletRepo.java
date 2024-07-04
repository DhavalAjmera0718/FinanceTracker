package com.personalfinancetracker.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import com.example.RestTemplate.Entity.AdharEntity;
import com.personalfinancetracker.enity.Wallet;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long>{
	
	
	Optional<Wallet> findByAccountNo(String accountNo);
	
//	Optional<Wallet> findByAdharNumber(String adharNumber);
	
	 @Query("SELECT a FROM Wallet a WHERE a.id = (SELECT MIN(b.id) FROM Wallet b WHERE b.adharNumber = :nadharNumber GROUP BY b.adharNumber HAVING COUNT(b.adharNumber) >= 1)")
	 Optional<Wallet> findByAdharNumber(@Param("nadharNumber") String nadharNumber);
	
	Optional<Wallet> findByBank(String bank);

}
