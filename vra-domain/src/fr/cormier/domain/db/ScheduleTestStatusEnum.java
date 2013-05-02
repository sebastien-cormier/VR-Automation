package fr.cormier.domain.db;

public enum ScheduleTestStatusEnum {

	TO_TEST("TO_TEST"), FAIL_1("FAIL_1"), FAIL_2("FAIL_2"), FAIL_3("FAIL_3"), CHECKED("CHECKED");
	
	private String value;
	
	private ScheduleTestStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
