package com.personalfinancetracker.Impli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.personalfinancetracker.tokencheck.TokenCheck;

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

	@Autowired
	private TokenCheck check;

	@Override

//	public String SaveWalletData(WalletProxy walletProxy) 
//	{
//		
//		HttpEntity<WalletProxy> walledProxy = new HttpEntity<>(walletProxy); 
//		
//		
//		String body = restTemplate.exchange("http://localhost:8080/adhar/saveAdharData", HttpMethod.POST, walledProxy, String.class).getBody();
//		
//			String randomString = CommonResponse.generateRandomString();
//			System.err.println("<-------- 11111111111111 -----------------> ");
//			
//			String encryptedAdhar = restTemplate.exchange("http://localhost:8080/adhar/getEncryptedRef/"+walletProxy.getAdharNumber(),
//					HttpMethod.GET, 
//					walledProxy, 
//					String.class).getBody();
//			System.err.println("<--------    22222222222    -----------------> ");
//			String getMatchIdFromRestTemplate = restTemplate.exchange("http://localhost:8080/adhar/getMatchid/"+walletProxy.getAdharNumber(), 
//					HttpMethod.GET,
//					walledProxy,
//					String.class).getBody(); 
//			System.err.println("<--------     3333333333333     -----------------> ");
			

	public String SaveWalletData(WalletProxy walletProxy, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == false) {
			HttpEntity<WalletProxy> walledProxy = new HttpEntity<>(walletProxy);

			String body = restTemplate
					.exchange("http://localhost:8080/adhar/saveAdharData", HttpMethod.POST, walledProxy, String.class)
					.getBody();

			System.err.println("BODY BEFORE SAVE IN WALLET TABEL ---> " + body);
			String randomString = CommonResponse.generateRandomString();

			String encryptedAdhar = restTemplate
					.exchange("http://localhost:8080/adhar/getEncryptedRef/" + walletProxy.getAdharNumber(),
							HttpMethod.GET, walledProxy, String.class)
					.getBody();

			String getMatchIdFromRestTemplate = restTemplate.exchange("http://localhost:8080/adhar/getMatchid/"+walletProxy.getAdharNumber(), 
					HttpMethod.GET,
					walledProxy,
					String.class).getBody(); 
			Optional<Wallet> byAdharNumber = walletRepo.findByAdharNumber(encryptedAdhar);
			System.err.println("XXXXXXXXXXXXXXXXXXX");
			Optional<Wallet> byBank = walletRepo.findByBank(walletProxy.getBank());
			System.err.println("<--------     4444444444444444       -----------------> ");
			
			if (byAdharNumber.isPresent() && byBank.isPresent()) {
				throw new RuntimeException("ADHAR NUMBER IS ALREADY REGISTERED..!!");
			}

			System.err.println("<--------   55555555555         -----------------> ");
			System.err.println("ENCRYPTED ADHAR FROM REST TEMPLATE----> " + encryptedAdhar);
			walletProxy.setMatchedId(getMatchIdFromRestTemplate);
			walletProxy.setAdharNumber(encryptedAdhar);
			walletProxy.setAccountNo(randomString);
			System.err.println("<--------       66666666666   ----------------> ");


			System.err.println("ENCRYPTED ADHAR FROM REST TEMPLATE----> " + encryptedAdhar);

			walletProxy.setAdharNumber(encryptedAdhar);
			walletProxy.setAccountNo(randomString);


			Wallet mainEntityData = walletHelper.ConvertDTO_To_Entity(walletProxy);
			System.err.println("<--------    777777777777777        -----------------> ");
			walletRepo.save(mainEntityData);

			return CommonResponse.WALLET_DATA_SAVE_RESPONSE + body;
		} else {
			return null;
		}
	}

	@Override
	public WalletProxy GetWalletDataByID(Long id, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
			Optional<Wallet> optionalWallet = walletRepo.findById(id);

			if (!optionalWallet.isPresent()) {
				throw new NoSuchElementException("Wallet not found for ID: " + id);
			}

			Wallet wallet = optionalWallet.get();
			WalletProxy walletProxy = walletHelper.ConvertEntity_To_DTO(wallet);
			System.err.println("WalletProxy From Implementation----> " + walletProxy);
			return walletProxy;
		} else {
			return null;
		}
	}

	/********************************************************
	 * [DELET WALLET DATA BY ID ]
	 ****************************************************************************************************/
	@Override
	public String DeleteWalletData(Long id, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
			walletRepo.deleteById(id);
			return id + CommonResponse.WALLET_DATA_DELETE_RESPONSE;
		} else {
			return null;
		}
	}

	/**************************************************************
	 * [UDPATE WALLET DATA BY ID ]
	 **********************************************************************************************************/

	public String UpdateWalletData(Long id, WalletProxy walletProxy, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
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
		} else {
			return null;
		}
	}

	/********************************************************
	 * [ DepositeMoney BY CASH]
	 *********************************************************************/
	@Override
	public String DepositeMoney(String accontNumber, TransactionDTO money, Map<String, String> headerData) {

		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
			Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accontNumber);
			System.out.println("by" + byAccountNo);

			if (byAccountNo.isPresent()) {

				Wallet wallet = byAccountNo.get();
				System.out.println("mobefore" + money.getBankDeposite());
				Double double1 = money.getBankDeposite();
				System.out.println("After" + double1);
				wallet.setBalance(wallet.getBalance() + double1);

				walletRepo.save(wallet);

				TransactionEntity transactionEntity = new TransactionEntity();
				transactionEntity.setTRname("Deposite");
				transactionEntity.setTRstatus("SUCCESS");
				transactionEntity.setWalletId(wallet.getId());
				transactionEntity.setTRsenderAccountNo("CASH");
				transactionEntity.setTransactionTime(CommonResponse.DateTimeFormatter());
				transactionEntity.setTRaccountNo(wallet.getAccountNo());
				transactionEntity.setTRaccountType(wallet.getAccountType());
				transactionEntity.setTRbalance(money.getBankDeposite());

				String response = " YOUR AC " + wallet.getAccountNo() + " IS CREDITED ON "
						+ CommonResponse.DateTimeFormatter() + " BY CASH" + " YOUR ACCOUNT BALANCE IS "
						+ wallet.getBalance() + " RS.";
				transactionEntity.setTRdescription(response);
				System.err.println("TRANSACTION ENTITY " + transactionEntity);
				transactionRepo.save(transactionEntity);
				System.err.println(response);
				return response;
			} else {
				return "Token Not Match";
			}
		}

		return "ACCOUNT NOT FOUND...";
	}

	/****************************************************************
	 * [ WITHDRAW CASH MONEY ]
	 **********************************************************************/
	@Override
	public String WithDrawMoney(String accountNumber, TransactionDTO money, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
			Optional<Wallet> byAccountNo = walletRepo.findByAccountNo(accountNumber);
			if (byAccountNo.isEmpty()) {
				throw new NoSuchElementException(
						"ACCOUNT NUMBER " + accountNumber + " IS NOT VALID PLEASE CHECK AGAIN...!!");
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
				String errorMessage = "Bank Account Number " + wallet.getAccountNo()
						+ " Don't Have sufficient Balance. Your Account Balance is " + wallet.getBalance();
				transactionEntity.setTRstatus("FAILED");
				transactionEntity.setTRdescription(errorMessage);
				transactionRepo.save(transactionEntity);
				throw new Error(errorMessage);
			}

			wallet.setBalance(availableBalance - withdrawAmount);
			String response = withdrawAmount + CommonResponse.WALLET_WITHDRAW + wallet.getAccountNo()
					+ " Your Account Balance Is " + wallet.getBalance();

			transactionEntity.setTRstatus("SUCCESS");
			transactionEntity.setTRdescription(response);

			walletRepo.save(wallet);
			transactionRepo.save(transactionEntity);

			return response;
		} else {
			return null;
		}
	}

	/*******************************************************
	 * [GET TRANSACTION BY BANK ID ]
	 **************************************************************************/

	public List<TransactionDTO> getDataByTransactionEntity(Long bankId, Map<String, String> headerData) {
		Boolean validateOrNot = check.validateOrNot(headerData);
		if (validateOrNot == true) {
			List<TransactionDTO> dtos = new ArrayList<>();
			List<TransactionEntity> byWalletId = transactionRepo.findByWalletId(bankId);
			for (TransactionEntity one : byWalletId) {
				TransactionDTO convertEntity_To_DTOTransaction = walletHelper.ConvertEntity_To_DTOTransaction(one);
				dtos.add(convertEntity_To_DTOTransaction);
			}
			return dtos;
		} else {
			return null;
		}
	}

}
