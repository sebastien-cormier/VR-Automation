package fr.cormier.domain;

import java.util.ArrayList;
import java.util.List;

public enum SailEnum {
	
	JIB(1),
	SPI(2);
	
	private int value;
	
	private SailEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static SailEnum fromInt(int sail)
	{
	    switch (sail) {
			case 1: return JIB;
			case 2: return SPI;
			default: return null;
			}
	}
	
	public static SailEnum fromString(String sail)
	{
		if("JIB".equalsIgnoreCase(sail)) {
			return JIB;
		} else if ("SPI".equalsIgnoreCase(sail)) {
			return SPI;
		} else {
			return null;
		}
	}
	
	public String getStringValue() {
	   return SailEnum.getStringValue(value);
	}
	
	public String toString() {
	    return getStringValue();
	}

	public static String getStringValue(int i) {
	    switch (i) {
			case 1: return "JIB";
			case 2: return "SPI";
			default: return null;
			}
	}
	
	public static List<SailEnum> getAllSails() {
		List<SailEnum> allSails = new ArrayList<SailEnum>();
		allSails.add(JIB);
		allSails.add(SPI);
		return allSails;
	}
	
}
