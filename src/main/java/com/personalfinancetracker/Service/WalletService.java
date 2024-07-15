package com.personalfinancetracker.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

@Service
public interface WalletService {

	
	public String SaveWalletData(WalletProxy walletProxy,Map<String, String> headerData);
	
	
//	public WalletProxy GetWalletDataByID(Long id,Map<String, String> headerData) ;
	public WalletProxy GetWalletDataByID(String id,Map<String, String> headerData) ;
	
	
	public String DeleteWalletData(Long id,Map<String, String> headerData) ;
	
	public String  UpdateWalletData(Long id , WalletProxy walletProxy,Map<String, String> headerData);
	
	
	public String DepositeMoney(String accontNumber , TransactionDTO money,Map<String, String> headerData);
	
	public String WithDrawMoney(String accontNumber , TransactionDTO money,Map<String, String> headerData) ;
	
//	public List<TransactionEntity>  getDataByTransactionEntity(Long bankId) ;
	
//	public List<TransactionDTO>  getDataByTransactionEntity(Long bankId,Map<String, String> headerData) ;
	
	public List<TransactionDTO>  getDataByTransactionEntity(String bankId,Map<String, String> headerData) ;
	
}
