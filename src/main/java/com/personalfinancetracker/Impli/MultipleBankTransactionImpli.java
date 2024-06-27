package com.personalfinancetracker.Impli;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.personalfinancetracker.Helper.CommonResponse;
import com.personalfinancetracker.Repo.MultipleBankTransactionRepo;
import com.personalfinancetracker.Repo.TransactionRepo;
import com.personalfinancetracker.Repo.WalletRepo;
import com.personalfinancetracker.Service.MultipleBankTransactionService;
import com.personalfinancetracker.enity.TransactionEntity;
import com.personalfinancetracker.enity.TransferInDiffBanks;
import com.personalfinancetracker.enity.Wallet;
import com.personalfinancetracker.proxy.MultiBankTransactionDTO;

@Component
public class MultipleBankTransactionImpli implements MultipleBankTransactionService{

	
	@Autowired
	private WalletRepo walletRepo;
	
	@Autowired
	private TransactionRepo tRepo;
	
	@Autowired
	private MultipleBankTransactionRepo multiBankTrRepo;
	
/***********************************[THIS FUNCTION IS FOR YOU CAN TRANSFER MONEY FROM YOUR TO  DIFFRENT ACCOUNT]*****************************************************************************/
	
	
	public String trMyBankToAnotherBank(String accountNo, MultiBankTransactionDTO mDto) throws Exception {
	    Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accountNo);
	    if (byAccountNo.isEmpty()) {
	        throw new NoSuchElementException("ACCOUNT NUMBER " + accountNo + " IS NOT VALID PLEASE CHECK AGAIN...!!");
	    }

	    Wallet wallet = byAccountNo.get();
	    Double currentBalance = wallet.getBalance();
	    Double transferAmount = mDto.getSendMoney();

	    TransactionEntity transactionEntity = new TransactionEntity();
	    transactionEntity.setTRname("Withdraw");
	    transactionEntity.setTRaccountNo(mDto.getReceiverBankAc());
	    transactionEntity.setTRbalance(transferAmount);
	    transactionEntity.setTRaccountType(wallet.getAccountType());
	    transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
	    transactionEntity.setTRsenderAccountNo(wallet.getAccountNo());
	    transactionEntity.setWalletId(wallet.getId());

	    if (currentBalance <= transferAmount) {
	        transactionEntity.setTRstatus("FAILED");
	        transactionEntity.setTRdescription(CommonResponse.INSUFIECIENT_BALANCE + wallet.getBalance() + "Rs.");
	        tRepo.save(transactionEntity);
	        throw new Exception(CommonResponse.INSUFIECIENT_BALANCE + wallet.getBalance() + "Rs.");
	    }

	    wallet.setBalance(currentBalance - transferAmount);
	    transactionEntity.setTRstatus("SUCCESS");
	    
	    String response = "YOUR AC " + wallet.getAccountNo() + " IS DEBITED BY " + transferAmount 
	                      + " ON " + CommonResponse.DateTimeFormatter() + " YOUR ACCOUNT BALANCE IS " 
	                      + wallet.getBalance() + " RS.";
	    
	    transactionEntity.setTRdescription(response);

	    walletRepo.save(wallet);
	    tRepo.save(transactionEntity);

	    return response;
	}

	
/*******************************************************[DEPOSITE FROM ANOTHER ACCOUNT ]*******************************************************************************/	
	
	public String DepositesFromAnotherAccount(String accountNo ,  MultiBankTransactionDTO mDto) 
	{
		
		Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accountNo);
		Wallet wallet = byAccountNo.get();
		TransactionEntity transactionEntity = new TransactionEntity();
		if (byAccountNo.isEmpty()) {
			transactionEntity.setTRstatus("Failed");
			throw new NoSuchElementException("ACCOUNT NUMBER - " +  accountNo +" IS IN VALID PLEASE CHECK. ");
		}
	
		wallet.setBalance(wallet.getBalance() +  mDto.getReceiveMoney());
		transactionEntity.setTRaccountNo(wallet.getAccountNo());
		transactionEntity.setTRbalance(mDto.getReceiveMoney());
		transactionEntity.setTRsenderAccountNo(mDto.getSenderBankAc());
		transactionEntity.setTRaccountType(wallet.getAccountType());
		transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
		transactionEntity.setWalletId(wallet.getId());
		
		String response = " YOUR AC " 
				+ wallet.getAccountNo()
				+ " IS CREDITED ON "
				+ CommonResponse.DateTimeFormatter()
				+ " BY "
				+	mDto.getSenderBankAc()
				+ " YOUR ACCOUNT BALANCE IS "
				+wallet.getBalance() + " RS.";
		System.err.println(response);
		
		transactionEntity.setTRdescription(response);
		transactionEntity.setTRname("Deposite");
		
		walletRepo.save(wallet);
		
		tRepo.save(transactionEntity);
		
		
		return response;
	}
	
}
