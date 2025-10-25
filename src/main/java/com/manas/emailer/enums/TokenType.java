package com.manas.emailer.enums;

public enum TokenType {
   ACCESS_TOKEN,
   REFRESH_TOKEN;
   public static TokenType safeValueOf(String tokenTypeHeader) {
	    if (tokenTypeHeader == null) {
	        return null; 
	    }
	    try {
	        return TokenType.valueOf(tokenTypeHeader.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        return null;
	    }
	}
}
