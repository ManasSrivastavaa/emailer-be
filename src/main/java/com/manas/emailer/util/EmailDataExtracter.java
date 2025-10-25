package com.manas.emailer.util;

public class EmailDataExtracter {


	public static String extractName(String email) {

		return email;
	}

	public static String extractCompany(String email) {
		return email.substring(email.indexOf("@")+1, email.lastIndexOf("."));
	}

}
