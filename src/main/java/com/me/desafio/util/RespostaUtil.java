package com.me.desafio.util;

import org.springframework.http.HttpStatus;

public class RespostaUtil {
	
	
	public static String montarMensagem(String message, HttpStatus internalServerError) {
		
		return "{\n"
				+ "  \"status\":"+ internalServerError+",\r\n"
				+ "  \"message\": "+message+"\n"
				+"}";
	}
}
