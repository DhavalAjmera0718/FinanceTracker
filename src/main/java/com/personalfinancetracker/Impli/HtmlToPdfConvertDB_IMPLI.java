package com.personalfinancetracker.Impli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.utils.PdfMerger;

import com.itextpdf.layout.element.Image;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageDataFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
	            System.err.println("TEXT -------- >  " + pdfText);
	            document.close();
	            
	            HtmlFormat htmlFormatFromDB3 = htmlTemplateRepo.findById(5L).get();
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
	                htmlCertificateTemplate3 = htmlCertificateTemplate3.replace("{{" + entry.getKey() + "}}" , entry.getKey());
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
	
/********************************************************[ DYNAMIC PDF CONVERTER ]************************************************************************/	
	 public byte[] mergePdfs(MultipartFile file) throws IOException {
	        // Create a new PDF document for the result
		
		 
		 // Create a new PDF document for the result
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    PdfWriter writer = new PdfWriter(outputStream);
		    PdfDocument pdfDoc = new PdfDocument(writer);

		    // Get the dimensions of the default page size
		    float originalPageWidth = pdfDoc.getDefaultPageSize().getWidth();
		    System.err.println("ORIGINAL WIDTH---- > " + originalPageWidth);
		    
		    float originalPageHeight = pdfDoc.getDefaultPageSize().getHeight();
		    
		    System.err.println("ORIGINAL HIGHT -----> " + originalPageHeight);
		    
		    // Adjust the height of the new page to fit both pages on one page
		    float newPageHeight = originalPageHeight * 2; // Adjust this as needed for better fit
		    
		    
		    System.err.println("NEW HIGHT---->  " + newPageHeight);

		    // Set the new page size
		    pdfDoc.setDefaultPageSize(new PageSize(originalPageWidth, newPageHeight));

		    // Create a Document instance for managing content
		    com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);
		    System.err.println("DOCUMENT ----- > " + document);

		    // Read content from the provided PDF
		    byte[] pdfBytes = file.getBytes();
		    PdfDocument inputPdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)));
		    
//		    ConverterProperties converterProperties =  new ConverterProperties();
//		    converterProperties.setCreateAcroForm(true);
//		    HtmlConverter.conve

		    // Check if the input PDF has exactly two pages
		    if (inputPdfDoc.getNumberOfPages() != 2) {
		        throw new IOException("The input PDF does not have exactly two pages.");
		    }

		    // Variables to track total content height
		    float totalContentHeight = 0;

		    // Scale and add content of both pages to the single page in the new PDF
		    for (int i = 1; i <= inputPdfDoc.getNumberOfPages(); i++) {
		        PdfPage page = inputPdfDoc.getPage(i);
		        PdfFormXObject pageCopy = page.copyAsFormXObject(pdfDoc);
		        Image pageImage = new Image(pageCopy);

		        System.err.println("PAGE IMAGE ---> " + pageImage);
		        // Calculate the scaling factor based on content height
		        float scaleFactor = (newPageHeight / 2) / originalPageHeight;
		        
		        System.err.println("SCALFACTOR ----> " + scaleFactor);

		        // Apply scaling
		        pageImage.scaleAbsolute(originalPageWidth, originalPageHeight * scaleFactor);

		        // Calculate the new height for this page
		        float contentHeight = originalPageHeight * scaleFactor;

		        // Check if the combined height exceeds the new page height
		        if (totalContentHeight + contentHeight > newPageHeight) {
		            throw new IOException("The combined height of the PDF content exceeds the new page height.");
		        }

		        // Set the position to top or bottom half of the page
		        if (i == 1) {
		            pageImage.setFixedPosition(0, newPageHeight / 2); // Top half
		        } else {
		            pageImage.setFixedPosition(0, 0); // Bottom half
		        }

		        document.add(pageImage);
		        totalContentHeight += contentHeight;
		    }

		    inputPdfDoc.close();
		    document.close();
		    
		    // Write the output to a file
		    try (FileOutputStream fos = new FileOutputStream(CommonResponse.CONVERT_PDF_INTO_1_PAGE)) {
		        fos.write(outputStream.toByteArray());
		    }
		    return outputStream.toByteArray();
	 }
	
}

















