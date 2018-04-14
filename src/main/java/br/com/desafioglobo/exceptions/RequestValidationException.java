package br.com.desafioglobo.exceptions;

public class RequestValidationException extends RuntimeException {

	private static final long serialVersionUID = 5318101658587179095L;

	public RequestValidationException(String message) {
		super(message);
	}
	
}
