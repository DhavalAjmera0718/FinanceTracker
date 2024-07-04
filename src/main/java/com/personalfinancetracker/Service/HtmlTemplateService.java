package com.personalfinancetracker.Service;

import org.springframework.stereotype.Service;

@Service
public interface HtmlTemplateService {
	
	
	public byte[]  ConvertDB_DataIntoPdfUsingHtml(String adharNumber) ;
}
