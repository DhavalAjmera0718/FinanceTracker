package com.personalfinancetracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personalfinancetracker.Service.MultipleBankTransactionService;
import com.personalfinancetracker.Service.WalletService;
import com.personalfinancetracker.enity.TransactionEntity;
import com.personalfinancetracker.proxy.MultiBankTransactionDTO;
import com.personalfinancetracker.proxy.TransactionDTO;
import com.personalfinancetracker.proxy.WalletProxy;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/wallet")
public class WalletController {

	
	
	@Autowired
	private WalletService service;
	
	@Autowired
	private MultipleBankTransactionService multiSevice;
	
	@PostMapping("/saveWalletData")
	public ResponseEntity<String> SaveWalletData(@RequestBody WalletProxy walletProxy) 
	{
		if (walletProxy!=null) {
			return new ResponseEntity<String>(service.SaveWalletData(walletProxy),HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("Please Check Your Data ....!!!" , HttpStatus.BAD_REQUEST);
	}
/******************************************************[WALLET GET BY ID ]***************************************************************************/	
	@GetMapping("/getWalletDataByid/{id}")
	public ResponseEntity<WalletProxy> GetWalletDataByID(@PathVariable("id") Long id) throws Exception 
	{
		if (id!=null) {
			
			return new ResponseEntity<WalletProxy>(service.GetWalletDataByID(id) , HttpStatus.OK);
		}
		throw new Exception();
	}
	
/********************************************************[DELET WALLET  DATA BY ID ] ****************************************************************************************************/	
	@PostMapping("deleteDataById/{id}")
	public ResponseEntity<String> DeleteWalletData(@PathVariable("id") Long id) throws Exception 
	{
		if (id !=null) {
			return new ResponseEntity<String>(service.DeleteWalletData(id) , HttpStatus.OK);
		}
		throw new Exception();
	}
	
/**************************************************************[UDPATE DATA BY ID ] **********************************************************************************************************/	
	@PostMapping("updateWalletData/{id}")
	public ResponseEntity<String>  UpdateWalletData(@PathVariable Long id , @RequestBody WalletProxy walletProxy) throws Exception 
	{
		if (id !=null) {
			return new ResponseEntity<String>(service.UpdateWalletData(id , walletProxy) , HttpStatus.OK);
		}
		throw new Exception();
	}	
	
/*********************************************************************[DEPOSITE MONEY]**************************************************************************************/
	@PostMapping("depositeMoney/{accontNumber}")
	public ResponseEntity<String> DepositeMoney(@PathVariable("accontNumber") String accontNumber ,@RequestBody TransactionDTO money) 
	{
		return new ResponseEntity<String>(service.DepositeMoney(accontNumber, money) , HttpStatus.ACCEPTED);
			
	}
	
/*********************************************************************[DEPOSITE MONEY]**************************************************************************************/
@PostMapping("withDrawMoney/{accontNumber}")
public ResponseEntity<String> withDrawMoney(@PathVariable("accontNumber") String accontNumber ,@RequestBody TransactionDTO money) 
	{
		return new ResponseEntity<String>(service.WithDrawMoney(accontNumber, money) , HttpStatus.ACCEPTED);
			
	}	

/*************************************************************[ MONEY TRANSFER MY WALLET TO  DIFFRENT BANK]****************************************************************************/
@PostMapping("myWalletToDiffWallet/{accountNo}")
public ResponseEntity<String> trMyBankToAnotherBank(@PathVariable("accountNo") String accountNo  ,@RequestBody MultiBankTransactionDTO multiBankTrDTO ) throws Exception 
{
	if (accountNo!=null) {
		return new ResponseEntity<String>(multiSevice.trMyBankToAnotherBank(accountNo, multiBankTrDTO), HttpStatus.OK);
	}
	return new ResponseEntity<String>("PLEASE CHECK YOUR BANK ACCOUNT NUMBER " ,  HttpStatus.BAD_REQUEST);
}

/*******************************************************[DEPOSITE FROM ANOTHER ACCOUNT ]*******************************************************************************/	
@PostMapping("DepositesFromAnotherAccount/{accountNo}")
public ResponseEntity<String> DepositesFromAnotherAccount(@PathVariable("accountNo") String accountNo ,@RequestBody  MultiBankTransactionDTO mDto) 
{
	
	if (accountNo!=null) {
		return new ResponseEntity<String>(multiSevice.DepositesFromAnotherAccount(accountNo, mDto) , HttpStatus.OK);
	}
	return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

}

@GetMapping("getDataByTransactionEntity/{bankId}")
public ResponseEntity<List<TransactionEntity>>  getDataByTransactionEntity(@PathVariable("bankId") Long bankId) 
{
	return new ResponseEntity<List<TransactionEntity>>(service.getDataByTransactionEntity(bankId) , HttpStatus.OK);
}



}

















