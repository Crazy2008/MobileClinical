package com.winning.mobileclinical.widget;

public class MathHelp {

	public static float parseFloat(String value){
		float f = 0.0f;
		if(value.equals("")){
			return f;
		}
		try {
			f = Float.parseFloat(value);
			return f;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0.0f;
		}
	}
	public static int parseInt(String value){
		int i = -1;
		if("".equals(value)){
			return i;
		}
		try {
			i = Integer.parseInt(value);
			return i;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
	 public static String subZeroAndDot(String s){  
	        if(s.indexOf(".") > 0){  
	            s = s.replaceAll("0+?$", "");//去掉多余的0  
	            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
	        }  
	        return s;  
	    }
}
