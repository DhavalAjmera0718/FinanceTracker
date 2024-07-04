package com.personalfinancetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personalfinancetracker.Repo.HtmlTemplateRepo;
import com.personalfinancetracker.Service.HtmlTemplateService;
import com.personalfinancetracker.Service.WalletService;

@RestController
public class HtmlToPdfController_DB {

	@Autowired
	private HtmlTemplateRepo htmlTemplateRepo;
	
	@Autowired
	private HtmlTemplateService htmlService;
	
/*****************************************[ {1} CONVERT ANY USER DATA FROM DATABASE INTO PDF IN LOCAL STORAGE ]**************************************************************************/
	
	@GetMapping("convertDbDataIntoPdfUsingHtmlTemplate/{adharNumber}")
	public ResponseEntity<byte[]>  ConvertDB_DataIntoPdfUsingHtml(@PathVariable("adharNumber") String adharNumber) 
	{
		
		try {
			byte[] pdfBytes = htmlService.ConvertDB_DataIntoPdfUsingHtml(adharNumber);
			
			HttpHeaders headers =  new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "converter.pdf");
			return ResponseEntity.ok()
					.headers(headers)
					.body(pdfBytes);
		
			
		} catch (Exception e) {
			System.err.println("Excepton Occur in controller----->  " + e.getMessage());
		}
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	
}
