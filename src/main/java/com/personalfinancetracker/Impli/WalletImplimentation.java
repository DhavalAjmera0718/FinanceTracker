package com.personalfinancetracker.Impli;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
/**************************************************************[UDPATE WALLET DATA BY ID ]**********************************************************************************************************/	
	
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
/********************************************************[ DepositeMoney BY CASH]*********************************************************************/
@Override
public String DepositeMoney(String accontNumber , TransactionDTO money) {
	
	 Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accontNumber);
	
	
	 if (byAccountNo.isPresent()) {
		
		Wallet wallet = byAccountNo.get();
		wallet.setBalance(wallet.getBalance() +  money.getBankDeposite());
		
		walletRepo.save(wallet);
		
		TransactionEntity transactionEntity =  new TransactionEntity();
		transactionEntity.setTRname("Deposite");
		transactionEntity.setTRstatus("Succsess");
		transactionEntity.setTRsenderAccountNo("CASH");
		transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
		transactionEntity.setTRaccountNo(wallet.getAccountNo());
		transactionEntity.setTRaccountType(wallet.getAccountType());
		transactionEntity.setTRbalance(money.getBankDeposite());
		
		String response = " YOUR AC " 
				+ wallet.getAccountNo()
				+ " IS CREDITED ON "
				+ CommonResponse.DateTimeFormatter()
				+ " BY CASH"
				+ " YOUR ACCOUNT BALANCE IS "
				+wallet.getBalance() + " RS.";
		transactionEntity.setTRdescription(response);
		System.err.println("TRANSACTION ENTITY " + transactionEntity);
		transactionRepo.save(transactionEntity);
		System.err.println(response);
		return response;	
	}
	
	return "ACCOUNT NOT FOUND...";
}
	
/**************************************************************** [ WITHDRAW  CASH MONEY ] **********************************************************************/
@Override
public String WithDrawMoney(String accontNumber , TransactionDTO money) {
	
	 Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accontNumber);
	if (byAccountNo.isEmpty()) {
		throw new NoSuchElementException("ACCOUNT NUMBER " + accontNumber +" IS NOT VALID PLEASE CHECK AGAIN...!!");
	}
		
		
		Wallet wallet = byAccountNo.get();
		TransactionEntity transactionEntity =  new TransactionEntity();
		
		String response	= money.getBankWithDraw() +  CommonResponse.WALLET_WITHDRAW + wallet.getAccountNo() + " Your Account Balance Is " + wallet.getBalance();	
		double availableBalance =  wallet.getBalance();
		double withDrawAmoubt = money.getBankWithDraw();
		
		if (availableBalance <= withDrawAmoubt) {
			transactionEntity.setTRstatus("FAILD");
			String errorMessage = "Bank Account Number " + wallet.getAccountNo() + " Don't Have sufficient Balance Your Account Balance is  "+ wallet.getBalance();
			transactionEntity.setTRdescription(errorMessage);
			System.err.println("IF PART--->");
		throw new Error("Bank Account Number " + wallet.getAccountNo() + " Don't Have sufficient Balance Your Account Balance is  "+ wallet.getBalance());
		}
		else {		
			System.err.println("ElSE PART ---> ");
			transactionEntity.setTRstatus("Succsess");
			wallet.setBalance(wallet.getBalance() -  money.getBankWithDraw());
			transactionEntity.setTRname("WITHDRAW");
			transactionEntity.setTRsenderAccountNo("CASH");
			transactionEntity.setTRaccountNo(wallet.getAccountNo());
			transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
			transactionEntity.setTRaccountType(wallet.getAccountType());
			transactionEntity.setTRbalance(money.getBankWithDraw());
			transactionEntity.setTRdescription(response);
			System.err.println("TRANSACTION ENTITY " + transactionEntity);
		}
			
		transactionRepo.save(transactionEntity);
		walletRepo.save(wallet);
		return response;
	
}

	
}
