package fr.cormier.domain.db;

public enum RaceStatusEnum {
	
	PENDING("PENDING"), OPEN("OPEN"), CLOSE("CLOSE");
	
	private String value;
	
	private RaceStatusEnum(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}

}
