package com.personalfinancetracker.Helper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class CommonResponse  implements PasswordEncoder {
	
	
	public static final String WALLET_DATA_SAVE_RESPONSE =  "WALLET DATA HASE BEEN SAVED....!!";
	
	public static final String WALLET_DATA_DELETE_RESPONSE =  " =  ID HAD BEEN DELETED....!!!";
	
	public static final String WALLET_DATA_UPDATE_RESPONSE =  " =  ID HAD BEEN UPDATED....!!!";
	
	public static final String WALLET_DEPOSITE =  " AMOUNT HAS BEEN DEPOSITED...!!!";
	
	public static final String WALLET_WITHDRAW =  " HAS BEEN DEDUCTED FROM ACCOUNT NO - ";
	
	public static final String INSUFIECIENT_BALANCE =  "INSUFIECIENT BALANCE IN YOUR ACCOUNT YOUR ACCOUNT BALANCE IS ";
	
	
	
	public static final String PDF_FILE_PATH = "../PersonalFinanceTracker/src/main/resources/templates/converter.pdf";
	
	public static final String HTML_TEMPLATE_OF_UAP_CERTIFICATE1="""
	          <html lang="en">
<head>
  <title>Wallet Details Certificate</title>
  <style>
    body {
      font-family: Arial, sans-serif;
    }

    .container {
      width: 80%;
      margin: 0 auto;
      border: 1px solid black;
      padding: 20px;
    }

    .header, .footer {
      text-align: center;
    }

    .header img {
      width: 50px;
      vertical-align: middle;
    }

    .header h1, .header h2, .header h3 {
      margin: 0;
    }

    .main-title {
      text-align: center;
      font-size: 24px;
      margin: 20px 0;
      text-decoration: underline;
    }

    .section {
      margin-bottom: 20px;
    }

    .section h4 {
      margin: 10px 0;
    }

    .section p {
      margin: 5px 0;
    }

    .table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
    }

    .table, .table th, .table td {
      border: 1px solid black;
    }

    .table th, .table td {
      padding: 5px;
      text-align: left;
    }

    .highlight {
      background-color: red;
      color: white;
      text-align: center;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <h4>INDIA</h4>
      <h2>Government of India</h2>
      <h3>Ministry of Finance</h3>
    </div>
    <div class="main-title">WALLET DETAILS CERTIFICATE</div>

    <div class="section">
      <h4>WALLET ID: <span>{{WALLET_ID}}</span></h4>
      <h4>WALLET NAME: <span>{{WALLET_NAME}}</span></h4>
      <h4>ACCOUNT NUMBER: <span>{{ACCOUNT_NUMBER}}</span></h4>
      <div class="highlight">
        Highlighted Information
      </div>
    </div>

    <div class="section">
      <h4>ACCOUNT TYPE: <span>{{ACCOUNT_TYPE}}</span></h4>
      <h4>BALANCE: <span>{{BALANCE}}</span></h4>

      <table class="table">
        <tr>
          <th>Serial Number</th>
          <th>Unit</th>
        </tr>
        <tr>
          <td>1</td>
          <td>{{WALLET_NAME}}</td>
        </tr>
      </table>
    </div>

    <div class="section">
      <h4>OFFICIAL DETAILS OF WALLET:</h4>
      <table class="table">
        <tr>
          <th>Adhar Number</th>
          <td>{{ADHAR_NUMBER}}</td>
          <th>Matched ID</th>
          <td>{{MATCHED_ID}}</td>
        </tr>
        <tr>
          <th>Bank</th>
          <td>{{BANK}}</td>
        </tr>
      </table>
    </div>

    <div class="footer">
      <p><b>Note: This is a system generated certificate and does not require any signature.</b></p>
      <h4>DATE OF INCORPORATION: <span>{{DATE_OF_INCORPORATION}}</span></h4>
    </div>
  </div>
</body>
</html>
	    """;
//********************************************************[css]*************************************************************************	
	public static final String CSS_STRING = "body {\r\n"
			+ "      font-family: Arial, sans-serif;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .container {\r\n"
			+ "      width: 80%;\r\n"
			+ "      margin: 0 auto;\r\n"
			+ "      border: 1px solid black;\r\n"
			+ "      padding: 20px;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .header, .footer {\r\n"
			+ "      text-align: center;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .header img {\r\n"
			+ "      width: 50px;\r\n"
			+ "      vertical-align: middle;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .header h1, .header h2, .header h3 {\r\n"
			+ "      margin: 0;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .main-title {\r\n"
			+ "      text-align: center;\r\n"
			+ "      font-size: 24px;\r\n"
			+ "      margin: 20px 0;\r\n"
			+ "      text-decoration: underline;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .section {\r\n"
			+ "      margin-bottom: 20px;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .section h4 {\r\n"
			+ "      margin: 10px 0;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .section p {\r\n"
			+ "      margin: 5px 0;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .table {\r\n"
			+ "      width: 100%;\r\n"
			+ "      border-collapse: collapse;\r\n"
			+ "      margin-top: 10px;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .table, .table th, .table td {\r\n"
			+ "      border: 1px solid black;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .table th, .table td {\r\n"
			+ "      padding: 5px;\r\n"
			+ "      text-align: left;\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "    .highlight {\r\n"
			+ "      background-color: red;\r\n"
			+ "      color: white;\r\n"
			+ "      text-align: center;\r\n"
			+ "    }";
	
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
/**********************************************************************************************************************************/
@Override
public String encode(CharSequence rawPassword) {
	
	return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
}
@Override
public boolean matches(CharSequence rawPassword, String encodedPassword) {
	// TODO Auto-generated method stub
	return false;
}

	
}
