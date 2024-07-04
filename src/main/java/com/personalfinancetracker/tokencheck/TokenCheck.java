package com.personalfinancetracker.tokencheck;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenCheck {

	@Autowired
	private RestTemplate restTemplate;

	public Boolean validateOrNot(Map<String, String> headerData) {
		HttpHeaders httpHeaders = new HttpHeaders();
		
		System.err.println(">>>>>>>>>>>>>>>>"+headerData.get("authorization"));
		httpHeaders.set("Authorization", headerData.get("authorization"));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
		Boolean body = restTemplate.exchange("http://localhost:8888/auth/verifyToken",HttpMethod.GET,httpEntity,Boolean.class).getBody();
		
//		Boolean body = restTemplate.getForObject("http://localhost:8888/auth/verifyToken", httpEntity, Boolean.class);
		return body;
	}
}
