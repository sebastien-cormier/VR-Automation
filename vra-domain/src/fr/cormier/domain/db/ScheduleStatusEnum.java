package fr.cormier.domain.db;

public enum ScheduleStatusEnum {
	
	PENDING("PENDING"), CURRENT("CURRENT"), FINISH("FINISH"), CANCELLED("CANCELLED");
	
	private String value;
	
	private ScheduleStatusEnum(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}

}
