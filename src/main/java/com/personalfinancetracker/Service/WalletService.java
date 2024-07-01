package com.personalfinancetracker.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.personalfinancetracker.enity.TransactionEntity;
import com.personalfinancetracker.proxy.MultiBankTransactionDTO;
import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

@Service
public interface WalletService {

	
	public String SaveWalletData(WalletProxy walletProxy);
	
	
	public WalletProxy GetWalletDataByID(Long id) ;
	
	
	public String DeleteWalletData(Long id) ;
	
	public String  UpdateWalletData(Long id , WalletProxy walletProxy) ;
	
	
	public String DepositeMoney(String accontNumber , TransactionDTO money);
	
	public String WithDrawMoney(String accontNumber , TransactionDTO money) ;
	
//	public List<TransactionEntity>  getDataByTransactionEntity(Long bankId) ;
	
	public List<TransactionDTO>  getDataByTransactionEntity(Long bankId) ;
	
	
}
