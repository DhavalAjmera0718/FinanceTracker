package com.personalfinancetracker.Service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface HtmlTemplateService {
	
	
	public byte[]  ConvertDB_DataIntoPdfUsingHtml(String adharNumber) ;

	public byte[]  PdfToPdfread(String adhar, MultipartFile multipartFile) ;
	
	 public byte[] mergePdfs(MultipartFile file1) throws IOException;

}


