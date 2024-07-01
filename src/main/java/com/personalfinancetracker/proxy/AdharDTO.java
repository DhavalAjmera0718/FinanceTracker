package com.personalfinancetracker.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AdharDTO {

	
	private Long adharId;
	
	private String adharNumber;
	
	private String adharEncryptedNo;
	
	private String adharName;
	
	
}
