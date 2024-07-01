package com.personalfinancetracker.Impli;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
	
	@Autowired
	private CommonResponse commonResponse;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String SaveWalletData(WalletProxy walletProxy) 
	{
		
		HttpEntity<WalletProxy> walledProxy = new HttpEntity<>(walletProxy); 
		
		
		String body = restTemplate.exchange("http://localhost:8080/adhar/saveAdharData", HttpMethod.POST, walledProxy, String.class).getBody();
		
		System.err.println("BODY BEFORE SAVE IN WALLET TABEL ---> " + body);
			String randomString = CommonResponse.generateRandomString();
			
			String encryptedAdhar = restTemplate.exchange("http://localhost:8080/adhar/getEncryptedRef/"+walletProxy.getAdharNumber(),
					HttpMethod.GET, 
					walledProxy, 
					String.class).getBody();
			
			Optional<Wallet> byAdharNumber = walletRepo.findByAdharNumber(encryptedAdhar);
			if (byAdharNumber.isPresent()) {
				throw new RuntimeException("ADHAR NUMBER IS ALREADY REGISTERED..!!");
			}
			
			System.err.println("ENCRYPTED ADHAR FROM REST TEMPLATE----> " + encryptedAdhar);
			
			walletProxy.setAdharNumber(encryptedAdhar);
			walletProxy.setAccountNo(randomString);
			
			Wallet mainEntityData = walletHelper.ConvertDTO_To_Entity(walletProxy);
			walletRepo.save(mainEntityData);
			
			return CommonResponse.WALLET_DATA_SAVE_RESPONSE + body;
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
		transactionEntity.setTRstatus("SUCCESS");
		transactionEntity.setWalletId(wallet.getId());
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
public String WithDrawMoney(String accountNumber, TransactionDTO money) {
    Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accountNumber);
    if (byAccountNo.isEmpty()) {
        throw new NoSuchElementException("ACCOUNT NUMBER " + accountNumber + " IS NOT VALID PLEASE CHECK AGAIN...!!");
    }

    Wallet wallet = byAccountNo.get();
    double availableBalance = wallet.getBalance();
    double withdrawAmount = money.getBankWithDraw();

    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setTRname("WITHDRAW");
    transactionEntity.setTRsenderAccountNo("CASH");
    transactionEntity.setTRaccountNo(wallet.getAccountNo());
    transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
    transactionEntity.setTRaccountType(wallet.getAccountType());
    transactionEntity.setTRbalance(withdrawAmount);
    transactionEntity.setWalletId(wallet.getId());

    if (availableBalance < withdrawAmount) { // Changed to '<' to avoid exact balance edge case
        String errorMessage = "Bank Account Number " + wallet.getAccountNo() + " Don't Have sufficient Balance. Your Account Balance is " + wallet.getBalance();
        transactionEntity.setTRstatus("FAILED");
        transactionEntity.setTRdescription(errorMessage);
        transactionRepo.save(transactionEntity);
        throw new Error(errorMessage);
    }

    wallet.setBalance(availableBalance - withdrawAmount);
    String response = withdrawAmount + CommonResponse.WALLET_WITHDRAW + wallet.getAccountNo() + " Your Account Balance Is " + wallet.getBalance();
    
    transactionEntity.setTRstatus("SUCCESS");
    transactionEntity.setTRdescription(response);
    
    walletRepo.save(wallet);
    transactionRepo.save(transactionEntity);

    return response;
}
/*******************************************************[GET TRANSACTION BY BANK ID ]**************************************************************************/	

	public List<TransactionEntity>  getDataByTransactionEntity(Long bankId) 
	{
		List<TransactionEntity> byWalletId = transactionRepo.findByWalletId(bankId);
		return byWalletId;
	}


}
