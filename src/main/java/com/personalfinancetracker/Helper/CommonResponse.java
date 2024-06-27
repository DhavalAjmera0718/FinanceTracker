package com.personalfinancetracker.Helper;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommonResponse {
	
	
	public static final String WALLET_DATA_SAVE_RESPONSE =  "WALLET DATA HASE BEEN SAVED....!!";
	
	public static final String WALLET_DATA_DELETE_RESPONSE =  " =  ID HAD BEEN DELETED....!!!";
	
	public static final String WALLET_DATA_UPDATE_RESPONSE =  " =  ID HAD BEEN UPDATED....!!!";
	
	public static final String WALLET_DEPOSITE =  " AMOUNT HAS BEEN DEPOSITED...!!!";
	
	public static final String WALLET_WITHDRAW =  " HAS BEEN DEDUCTED FROM ACCOUNT NO - ";
	
	public static final String INSUFIECIENT_BALANCE =  "INSUFIECIENT BALANCE IN YOUR ACCOUNT YOUR ACCOUNT BALANCE IS ";
	
	
	
	
	
/********************************************************[RANDOM STRING GENRATOR ]****************************************************************************/	
	private static final String CHARACTERS = "0123456789";
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
/********************************************************[ DATE AND TIME FORMATER ]********************************************************************************/	
	
	  public static String DateTimeFormatter() 
	  {
		  LocalDateTime nowDateTime = LocalDateTime.now();
		  
		  DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		  
		   return nowDateTime.format(formatter);
		  
		  
	  }
	
}
