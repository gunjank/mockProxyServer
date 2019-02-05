package com.bh.api.proxy.gateway.ui;

public class ValidationException extends Exception{
	private static final long serialVersionUID = 1L;
	
	String code;

	public ValidationException(String description, String code) {
		super(description);
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
