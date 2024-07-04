package com.personalfinancetracker.Impli;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.personalfinancetracker.Helper.CommonResponse;
import com.personalfinancetracker.Helper.HtmlDataSetFromTable;
import com.personalfinancetracker.Repo.HtmlTemplateRepo;
import com.personalfinancetracker.Repo.WalletRepo;
import com.personalfinancetracker.Service.HtmlTemplateService;
import com.personalfinancetracker.enity.HtmlFormat;

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
			
			HtmlFormat htmlTemplate1 = htmlTemplateRepo.findById(1L).get();
			
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
	
	
}

















