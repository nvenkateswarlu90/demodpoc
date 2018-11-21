package com.a4tech.shipping.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadBean {
	private MultipartFile file ;
	private String asiNumber;
    private String environmentType;
	
	public String getAsiNumber() {
		return asiNumber;
	}

	public void setAsiNumber(String asiNumber) {
		this.asiNumber = asiNumber;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(String environmentType) {
		this.environmentType = environmentType;
	}

}
