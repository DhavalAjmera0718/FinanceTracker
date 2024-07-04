package com.personalfinancetracker.Service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.personalfinancetracker.proxy.MultiBankTransactionDTO;

@Service
public interface MultipleBankTransactionService {

	
	public String trMyBankToAnotherBank(String accountNo  , MultiBankTransactionDTO multiBankTrDTO,Map<String, String> headerData ) throws Exception ;
	
	public String DepositesFromAnotherAccount(String accountNo ,  MultiBankTransactionDTO mDto,Map<String, String> headerData) ;
}
