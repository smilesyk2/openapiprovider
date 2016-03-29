package com.wisenut.openapi.util;

public class StringUtil {
	public static String removeSpecialCharacter(String str){
		str = str.replaceAll("\n", "");
		str = str.replaceAll("\r", "");
		str = str.replaceAll("\"", "&quot;");
		
		return str;
	}
}
