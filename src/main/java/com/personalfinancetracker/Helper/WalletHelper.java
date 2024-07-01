package com.personalfinancetracker.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalfinancetracker.enity.TransactionEntity;
import com.personalfinancetracker.enity.Wallet;
import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

@Component
public class WalletHelper {

	@Autowired
	private ObjectMapper objectMapper;
	
	
/*****************************************************[CONVERT DTO  TO ENTITY ]**********************************************************************************/	
	public Wallet ConvertDTO_To_Entity (WalletProxy walletProxy) 
	{
		return objectMapper.convertValue(walletProxy, Wallet.class);
	}
	
/*****************************************************[CONVERT ENTITY  TO DTO ]**********************************************************************************/	
	public WalletProxy ConvertEntity_To_DTO (Wallet wallet) 
	{
		return objectMapper.convertValue(wallet, WalletProxy.class);
	}	
	
	public TransactionDTO ConvertEntity_To_DTOTransaction(TransactionEntity entity){
		return objectMapper.convertValue(entity, TransactionDTO.class);
	}
	
	
}
