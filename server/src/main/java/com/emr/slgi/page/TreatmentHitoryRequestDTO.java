package com.emr.slgi.page;

import lombok.Data;

@Data
public class TreatmentHitoryRequestDTO {
	private int pageNo = 1;
	private int size = 5;	
	private String doctorUuid; 
	private String patientUuid;
	 
	 public	int getStart() {
		 return (pageNo - 1) * size ;
	 }
	 
}