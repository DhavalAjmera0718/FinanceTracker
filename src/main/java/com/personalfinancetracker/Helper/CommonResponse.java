package com.personalfinancetracker.Helper;

import java.security.SecureRandom;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class CommonResponse {
	
	
	public static final String WALLET_DATA_SAVE_RESPONSE =  "WALLET DATA HASE BEEN SAVED....!!";
	
	public static final String WALLET_DATA_DELETE_RESPONSE =  " =  ID HAD BEEN DELETED....!!!";
	
	public static final String WALLET_DATA_UPDATE_RESPONSE =  " =  ID HAD BEEN UPDATED....!!!";
	
	public static final String WALLET_DEPOSITE =  " AMOUNT HAS BEEN DEPOSITED...!!!";
	
	public static final String WALLET_WITHDRAW =  " HAS BEEN DEDUCTED FROM ACCOUNT NO - ";
	
	
	
	
/********************************************************[RANDOM STRING GENRATOR ]****************************************************************************/	
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	  private static final SecureRandom RANDOM = new SecureRandom();
	  private static final String BANK_CODE = "PUNB";

	
	  
	  public static String generateRandomString() {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < 20; i++) {
	            int index = RANDOM.nextInt(CHARACTERS.length());
	            sb.append(CHARACTERS.charAt(index));
	        }
	        return BANK_CODE + sb.toString();
	    }
	
	
	
}
