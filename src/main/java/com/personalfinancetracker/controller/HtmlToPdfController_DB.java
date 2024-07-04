package com.personalfinancetracker.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.personalfinancetracker.Helper.CommonResponse;
import com.personalfinancetracker.Repo.HtmlTemplateRepo;
import com.personalfinancetracker.Service.HtmlTemplateService;

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
/****************************************************[ {2} CONVERT ABOVE GENRATED PDF TO FORMATED PDF]**************************************************************************/	
	
	@GetMapping("pdfToPdfread/{adharnumber}")
	public ResponseEntity<byte[]>  PdfToPdfread(@RequestParam("file") MultipartFile multipartFile , @PathVariable("adharnumber") String adhar) 
	{
		
		try {
			byte[] pdfToPdfread = htmlService.PdfToPdfread(adhar, multipartFile);
			
			HttpHeaders headers =  new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("attachment", "Modified.pdf");
			return ResponseEntity.ok()
					.headers(headers)
					.body(pdfToPdfread);
			
		} catch (Exception e) {
			System.err.println("Excepton Occur in controller----->  " + e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
	}
	
	public static final String FIRST_PDF_PATH = "C:/Users/DhavalAjmera/git/PersonalFinanceTracker/src/main/resources/templates.converter.pdf";
	
	public static final String SECOND_PDF_PATH = "C:/Users/DhavalAjmera/git/PersonalFinanceTracker/src/main/resources/templates/Modified.pdf";
	
	
	@PostMapping("/create")
	public ResponseEntity<String> createPdfFromBytes(@RequestParam("filePath") String filePath,
	                                                 @RequestParam("pdfBytes") MultipartFile pdfFile) {

//		  try {
//	            byte[] pdfBytes = pdfFile.getBytes();
//	            String string  =  new String(pdfBytes,StandardCharsets.UTF_8);
//	            
//	            
//	            System.err.println("PDF IN STRING------>  " + string);
//	            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
//	                fos.write(pdfBytes);
//	            }
//	            return ResponseEntity.ok("PDF created successfully");
//	        } catch (IOException e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create PDF");
//	        }
		
		try {
	        byte[] pdfBytes = pdfFile.getBytes();
	        
	        // Load the PDF document from the bytes
	        PDDocument originalDocument = PDDocument.load(pdfBytes);
	        
	        // Create a new PDF document for the first page
	        PDDocument newDocument = new PDDocument();
	        
	        // Get the pages from the original document
	        PDPageTree pages = originalDocument.getPages();
	        Iterator<PDPage> iterator = pages.iterator();
	        
	        // Add the first page to the new document if it exists
	        if (iterator.hasNext()) {
	            PDPage firstPage = iterator.next();
	            newDocument.addPage(firstPage);
	        }
	        
	        // Save the new PDF document
	        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
	            newDocument.save(fos);
	        }
	        
	        // Close the documents
	        originalDocument.close();
	        newDocument.close();
	        
	        return ResponseEntity.ok("PDF created successfully with the first page only");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create PDF");
	    }
	}

	private String convertTextToHtmlWithCss(String text) {
	    Document doc = Jsoup.parse("");
	    Element head = doc.head();
	    Element style = new Element(Tag.valueOf("style"), "");
	    
	    // Add your CSS styles here
	    String css = CommonResponse.CSS_STRING;
	    style.appendText(css);
	    head.appendChild(style);

	    Element body = doc.body();
	    String[] lines = text.split("\r\n|\r|\n");
	    for (String line : lines) {
	        Element p = new Element(Tag.valueOf("p"), "");
	        p.text(line);
	        body.appendChild(p);
	    }
	    System.err.println("HTML---------------> " + doc.outerHtml());
	    
	    return doc.outerHtml();
	}
	
}
