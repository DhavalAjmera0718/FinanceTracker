package com.personalfinancetracker.Impli;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.personalfinancetracker.Helper.CommonResponse;
import com.personalfinancetracker.Helper.WalletHelper;
import com.personalfinancetracker.Repo.TransactionRepo;
import com.personalfinancetracker.Repo.WalletRepo;
import com.personalfinancetracker.Service.WalletService;
import com.personalfinancetracker.enity.TransactionEntity;
import com.personalfinancetracker.enity.Wallet;
import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

@Component
public class WalletImplimentation implements WalletService {

	@Autowired
	private WalletRepo walletRepo;
	
	
	@Autowired
	private WalletHelper walletHelper;
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Override
	public String SaveWalletData(WalletProxy walletProxy) 
	{
			String randomString = CommonResponse.generateRandomString();
			System.err.println("RANDOM STRING FOR BANK ACCOUNT------> " + randomString);
			walletProxy.setAccountNo(randomString);
			
			Wallet mainEntityData = walletHelper.ConvertDTO_To_Entity(walletProxy);
			walletRepo.save(mainEntityData);
			
			return CommonResponse.WALLET_DATA_SAVE_RESPONSE;
	}
	
	@Override
	public WalletProxy GetWalletDataByID(Long id) 
	{
		Optional<Wallet> optionalWallet = walletRepo.findById(id);

	    if (!optionalWallet.isPresent()) {
	        throw new NoSuchElementException("Wallet not found for ID: " + id);
	    }

	    Wallet wallet = optionalWallet.get();
	    WalletProxy walletProxy = walletHelper.ConvertEntity_To_DTO(wallet);
	    System.err.println("WalletProxy From Implementation----> " + walletProxy);
	    return walletProxy;
	}
	
/********************************************************[DELET WALLET  DATA BY ID ]****************************************************************************************************/	
	@Override
	public String DeleteWalletData(Long id) 
	{
		walletRepo.deleteById(id);
		return id +  CommonResponse.WALLET_DATA_DELETE_RESPONSE;
	}
	
/**************************************************************[UDPATE DATA BY ID ]**********************************************************************************************************/	
	
	public String  UpdateWalletData(Long id , WalletProxy walletProxy) 
	{
		Optional<Wallet> byId = walletRepo.findById(id);
		if (byId.isEmpty()) {
			throw new NoSuchElementException("Wallet not found for ID: " + id);
		}
		Wallet wallet = byId.get();
		wallet.setAccountType(walletProxy.getAccountType());
		wallet.setBalance(walletProxy.getBalance());
		wallet.setName(walletProxy.getName());
		walletRepo.save(wallet);
		
		
		return id + CommonResponse.WALLET_DATA_UPDATE_RESPONSE;
		
	}
/*****************************************************************************************************************************/
@Override
public String DepositeMoney(String accontNumber , TransactionDTO money) {
	
	 Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accontNumber);
	if (byAccountNo.isPresent()) {
		
		
		
		Wallet wallet = byAccountNo.get();
		wallet.setBalance(wallet.getBalance() +  money.getBankDeposite());
		
		walletRepo.save(wallet);
		
		TransactionEntity transactionEntity =  new TransactionEntity();
		transactionEntity.setTRname("Deposite");
		transactionEntity.setTRaccountNo(wallet.getAccountNo());
		transactionEntity.setTRaccountType(wallet.getAccountType());
		transactionEntity.setTRbalance(money.getBankDeposite());
		System.err.println("TRANSACTION ENTITY " + transactionEntity);
		transactionRepo.save(transactionEntity);

		return CommonResponse.WALLET_DEPOSITE;	
	}
	return "ACCOUNT NOT FOUND...";
}
	

@Override
public String WithDrawMoney(String accontNumber , TransactionDTO money) {
	
	 Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accontNumber);
	if (byAccountNo.isPresent()) {
		
		
		
		Wallet wallet = byAccountNo.get();
		
		double availableBalance =  wallet.getBalance();
		double withDrawAmoubt = money.getBankWithDraw();
		
		if (availableBalance <= withDrawAmoubt) {
			
		return "Bank Account Number " + wallet.getAccountNo() + " Don't Have sufficient Balance Your Account Balance is  "+ wallet.getBalance();
		}
		wallet.setBalance(wallet.getBalance() -  money.getBankWithDraw());
		
		walletRepo.save(wallet);
		
		TransactionEntity transactionEntity =  new TransactionEntity();
		transactionEntity.setTRname("WITHDRAW");
		transactionEntity.setTRaccountNo(wallet.getAccountNo());
		transactionEntity.setTRaccountType(wallet.getAccountType());
		transactionEntity.setTRbalance(money.getBankWithDraw());
		System.err.println("TRANSACTION ENTITY " + transactionEntity);
		transactionRepo.save(transactionEntity);

		return money.getBankWithDraw() +  CommonResponse.WALLET_WITHDRAW + wallet.getAccountNo() + " Your Account Balance Is " + wallet.getBalance();	
	}
	return "ACCOUNT NOT FOUND...";
}
	
}
