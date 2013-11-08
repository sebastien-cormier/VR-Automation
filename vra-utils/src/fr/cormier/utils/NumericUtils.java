package fr.cormier.utils;


public class NumericUtils {

	public static boolean isInteger(String s) {
		try{
			  Integer.parseInt(s);
			  return true;
			} catch (NumberFormatException e) {
			  return false;
			}
	}
}
