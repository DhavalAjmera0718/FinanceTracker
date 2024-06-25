package com.personalfinancetracker.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personalfinancetracker.enity.TransactionEntity;

public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {

}
