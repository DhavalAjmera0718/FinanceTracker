package com.personalfinancetracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personalfinancetracker.Service.MultipleBankTransactionService;
import com.personalfinancetracker.Service.WalletService;
import com.personalfinancetracker.proxy.MultiBankTransactionDTO;
import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private WalletService service;

	@Autowired
	private MultipleBankTransactionService multiSevice;

	@PostMapping("/saveWalletData")
	public ResponseEntity<String> SaveWalletData(@RequestBody WalletProxy walletProxy,@RequestHeader Map<String, String> headerdata) 
	{
		if (walletProxy!=null &&  walletProxy.getBalance()>=5000.00 && walletProxy.getAccountType().equals("Current")
				|| walletProxy!=null &&  walletProxy.getBalance()>=1000.00 && walletProxy.getAccountType().equals("Saving")) {
			return new ResponseEntity<String>(service.SaveWalletData(walletProxy,headerdata),HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("Please Check Your Data ....!!!" , HttpStatus.BAD_REQUEST);
	}

	/******************************************************* [WALLET GET BY ID ]***************************************************************************/
	@GetMapping("/getWalletDataByid/{id}")
	public ResponseEntity<WalletProxy> GetWalletDataByID(@PathVariable("id") Long id,@RequestHeader Map<String, String> headerdata) throws Exception {
		if (id != null) {

			return new ResponseEntity<WalletProxy>(service.GetWalletDataByID(id,headerdata), HttpStatus.OK);
		}
		throw new Exception();
	}

	/******************************************************** * [DELET WALLET DATA BY ID ]****************************************************************************************************/
	@PostMapping("deleteDataById/{id}")
	public ResponseEntity<String> DeleteWalletData(@PathVariable("id") Long id,@RequestHeader Map<String, String> headerdata) throws Exception {
		if (id != null) {
			return new ResponseEntity<String>(service.DeleteWalletData(id,headerdata), HttpStatus.OK);
		}
		throw new Exception();
	}

	/*************************************************************** [UDPATE DATA BY ID ]**********************************************************************************************************/
	@PostMapping("updateWalletData/{id}")
	public ResponseEntity<String> UpdateWalletData(@PathVariable Long id, @RequestBody WalletProxy walletProxy,@RequestHeader Map<String, String> headerdata)
			throws Exception {
		if (id != null) {
			return new ResponseEntity<String>(service.UpdateWalletData(id, walletProxy,headerdata), HttpStatus.OK);
		}
		throw new Exception();
	}

	/********************************************************************** [DEPOSITE MONEY]**************************************************************************************/
	@PostMapping("depositeMoney/{accontNumber}")
	public ResponseEntity<String> DepositeMoney(@PathVariable("accontNumber") String accontNumber,
			@RequestBody TransactionDTO money,@RequestHeader Map<String, String> headerdata) {
		System.out.println("Money"+headerdata);
		return new ResponseEntity<String>(service.DepositeMoney(accontNumber, money,headerdata), HttpStatus.ACCEPTED);

	}

	/********************************************************************* * [DEPOSITE MONEY]**************************************************************************************/
	@PostMapping("withDrawMoney/{accontNumber}")
	public ResponseEntity<String> withDrawMoney(@PathVariable("accontNumber") String accontNumber,
			@RequestBody TransactionDTO money,@RequestHeader Map<String, String> headerdata) {
		return new ResponseEntity<String>(service.WithDrawMoney(accontNumber, money,headerdata), HttpStatus.ACCEPTED);

	}

	/************************************************************** [ MONEY TRANSFER MY WALLET TO DIFFRENT BANK]****************************************************************************/
	@PostMapping("myWalletToDiffWallet/{accountNo}")
	public ResponseEntity<String> trMyBankToAnotherBank(@PathVariable("accountNo") String accountNo,
			@RequestBody MultiBankTransactionDTO multiBankTrDTO,@RequestHeader Map<String, String> headerdata) throws Exception {
		if (accountNo != null) {
			return new ResponseEntity<String>(multiSevice.trMyBankToAnotherBank(accountNo, multiBankTrDTO,headerdata),
					HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("PLEASE CHECK YOUR BANK ACCOUNT NUMBER ", HttpStatus.BAD_REQUEST);
	}

	/******************************************************* * [DEPOSITE FROM ANOTHER ACCOUNT ] *******************************************************************************/
	@PostMapping("DepositesFromAnotherAccount/{accountNo}")
	public ResponseEntity<String> DepositesFromAnotherAccount(@PathVariable("accountNo") String accountNo,
			@RequestBody MultiBankTransactionDTO mDto,@RequestHeader Map<String, String> headerdata) {

		if (accountNo != null) {
			return new ResponseEntity<String>(multiSevice.DepositesFromAnotherAccount(accountNo, mDto,headerdata), HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	}

	@GetMapping("getDataByTransactionEntity/{bankId}")
	public ResponseEntity<List<TransactionDTO>> getDataByTransactionEntity(@PathVariable("bankId") Long bankId,@RequestHeader Map<String, String> headerdata) {
		return new ResponseEntity<List<TransactionDTO>>(service.getDataByTransactionEntity(bankId,headerdata), HttpStatus.OK);
	}

}
