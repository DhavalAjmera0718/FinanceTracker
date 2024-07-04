package com.personalfinancetracker.Impli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.personalfinancetracker.Helper.CommonResponse;
import com.personalfinancetracker.Helper.HtmlDataSetFromTable;
import com.personalfinancetracker.Repo.HtmlTemplateRepo;
import com.personalfinancetracker.Repo.WalletRepo;
import com.personalfinancetracker.Service.HtmlTemplateService;
import com.personalfinancetracker.enity.HtmlFormat;

import jakarta.mail.Multipart;

@Component
public class HtmlToPdfConvertDB_IMPLI implements HtmlTemplateService{

	@Autowired
	private HtmlTemplateRepo htmlTemplateRepo;
	
	@Autowired
	private HtmlDataSetFromTable htmlHelper;
	
	@Autowired
	private WalletRepo walletRepo;
	

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public byte[]  ConvertDB_DataIntoPdfUsingHtml(String adharNumber) 
	{
		try (ByteArrayOutputStream outputStream =  new ByteArrayOutputStream()){
			
			HtmlFormat htmlTemplate1 = htmlTemplateRepo.findById(2L).get();
//			HtmlFormat htmlTemplate1 = htmlTemplateRepo.findById(3L).get();
//			HtmlFormat htmlTemplate1 = htmlTemplateRepo.findById(4L).get();
			
			String htmlTemplate = htmlTemplate1.getHtmlCertificateTemplate();
			
			

//			String htmlTemplate = CommonResponse.HTML_TEMPLATE_OF_UAP_CERTIFICATE1;
			
			System.err.println("HTM CODE------->  " + htmlTemplate);
			
			String encryptedAdhar = restTemplate.exchange("http://localhost:8080/adhar/getEncryptedRef/"+adharNumber,
					HttpMethod.GET, 
					null, 
					String.class).getBody();
			System.err.println("ENCRYPTED ADHAR ----> "  + encryptedAdhar);
			Map<String, String> walletData = htmlHelper.getWalletData(encryptedAdhar);
			
			System.err.println("WALTED DATA ---->  " + walletData);
			
			for (Map.Entry<String ,String> entry : walletData.entrySet()) {
				
				htmlTemplate = htmlTemplate.replace("{{" + entry.getKey()+"}}", entry.getValue());
				
			}
			Document document = Jsoup.parse(htmlTemplate);
			PdfRendererBuilder builder =  new PdfRendererBuilder();
			builder.withHtmlContent(document.outerHtml(),"");
			builder.toStream(outputStream);
//			 builder.useDefaultPageSize(PdfRendererBuilder.PageSize.A4);
			builder.run();
			
			
			Path filPath =  Paths.get(CommonResponse.PDF_FILE_PATH);
			Files.write(filPath, outputStream.toByteArray());
			
			String message = "PDF SAVED TO  " + CommonResponse.PDF_FILE_PATH;
			byte[] messageInbytes = message.getBytes(StandardCharsets.UTF_8);
			
			return outputStream.toByteArray();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("CATCH FROM GETPDF IMPLIMANTATION " + e.getMessage());
	        e.getMessage();
		}
		return null;
	}
	
	@Override
	public byte[]  PdfToPdfread(String adharNumber , MultipartFile multipartFile) 
	{
		try {
			byte[] pdfBytes = multipartFile.getBytes();
			 PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes));
	            PDFTextStripper pdfStripper = new PDFTextStripper();
	            String pdfText = pdfStripper.getText(document);
	            document.close();
	            
	            HtmlFormat htmlFormatFromDB3 = htmlTemplateRepo.findById(3L).get();
	            String htmlCertificateTemplate3 = htmlFormatFromDB3.getHtmlCertificateTemplate();
	            HtmlFormat htmlFormatFromDB = htmlTemplateRepo.findById(2L).get();
	            String htmlCertificateTemplate = htmlFormatFromDB.getHtmlCertificateTemplate();
	            
	            
	            String encryptedAdhar = restTemplate.exchange("http://localhost:8080/adhar/getEncryptedRef/"+adharNumber,
						HttpMethod.GET, 
						null, 
						String.class).getBody();
	            Map<String, String> walletData = htmlHelper.getWalletData(encryptedAdhar);
	            
	            // Replace placeholders in the HTML template
	            for (Map.Entry<String, String> entry : walletData.entrySet()) {
	                htmlCertificateTemplate3 = htmlCertificateTemplate3.replace("{{" + entry.getKey() + "}}", entry.getValue());
	            }

	            // Replace a specific placeholder with the extracted PDF text
	            htmlCertificateTemplate3 = htmlCertificateTemplate3.replace("{{PDF_CONTENT}}", pdfText);
	            
//	            String modiFiedHTml = htmlCertificateTemplate.replace(htmlCertificateTemplate3, pdfText);
	            
	            ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
	            ITextRenderer renderer =  new ITextRenderer();
	            Document HtmlDocument = Jsoup.parse(htmlCertificateTemplate3);
	            renderer.setDocumentFromString(HtmlDocument.outerHtml());
	            renderer.layout();
	            renderer.createPDF(outputStream);
	            
	            Path filPath =  Paths.get(CommonResponse.PDF_FILE_PATH);
				Files.write(filPath, outputStream.toByteArray());
	            
	           return outputStream.toByteArray();
	            
	            
	            
		} catch (Exception e) {
			System.err.println("ERROR COMES FROM CATCH------->  " + e.getMessage());
			return null;
			// TODO: handle exception
		}
	}
	
}

















