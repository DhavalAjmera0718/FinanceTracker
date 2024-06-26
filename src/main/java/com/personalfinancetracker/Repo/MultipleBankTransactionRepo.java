package com.personalfinancetracker.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personalfinancetracker.enity.TransferInDiffBanks;

@Repository
public interface MultipleBankTransactionRepo  extends JpaRepository<TransferInDiffBanks, Long>{

}
