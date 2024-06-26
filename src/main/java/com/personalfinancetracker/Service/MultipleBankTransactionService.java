package com.personalfinancetracker.Service;

import org.springframework.stereotype.Service;

import com.personalfinancetracker.proxy.MultiBankTransactionDTO;

@Service
public interface MultipleBankTransactionService {

	
	public String trMyBankToAnotherBank(String accountNo  , MultiBankTransactionDTO multiBankTrDTO ) throws Exception ;
	
	public String DepositesFromAnotherAccount(String accountNo ,  MultiBankTransactionDTO mDto) ;
}
