package com.personalfinancetracker.Helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.personalfinancetracker.Repo.WalletRepo;
import com.personalfinancetracker.enity.Wallet;

@Component
public class HtmlDataSetFromTable {

	@Autowired
	private WalletRepo walletRepo;
	
	public Map<String, String> getWalletData(String id) {
	    try {
	        Wallet findById = walletRepo.findByAdharNumber(id).orElse(null);
	        System.err.println("*****************" + findById);
	        if (findById != null) {
	            Map<String, String> data = new HashMap<>();
//	            data.put("WALLET_ID", findById.getId());
	            data.put("WALLET_NAME", findById.getName());
	            data.put("ACCOUNT_NUMBER", findById.getAccountNo());
	            data.put("ACCOUNT_TYPE", findById.getAccountType());
	            data.put("BALANCE", findById.getBalance().toString());
	            data.put("ADHAR_NUMBER", findById.getAdharNumber());
	            data.put("MATCHED_ID", "11111");
	            data.put("BANK", findById.getBank());
	            
	            System.err.println("DATA--------->" + data);
	            return data;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	    }
	    return null;
	}
	
	
}
