package fr.cormier.domain.db;

import fr.cormier.utils.EqualsUtils;

public class Command {
		
	private int commandId;
	
	private CommandTypeEnum commandType;
	
	private int raceId;
	
	private int vrUserId;
	
	private String value;
	
	private String r;
	
	private String checksum;

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public CommandTypeEnum getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandTypeEnum commandType) {
		this.commandType = commandType;
	}

	public int getVrUserId() {
		return vrUserId;
	}

	public void setVrUserId(int userId) {
		this.vrUserId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	@Override
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof Command) ) return false;
		Command command = (Command)object;
		return 
				EqualsUtils.areEqual(this.commandId, command.commandId) &&
				EqualsUtils.areEqual(this.vrUserId, command.vrUserId) &&
				EqualsUtils.areEqual(this.raceId, command.raceId) &&
				EqualsUtils.areEqual(this.commandType, command.commandType) &&
				EqualsUtils.areEqual(this.value, command.value) &&
				EqualsUtils.areEqual(this.checksum, command.checksum) &&
				EqualsUtils.areEqual(this.r, command.r);
	}
	
	public String toString() {
		return "Command("+commandId+","+commandType+"("+value+"), user "+vrUserId+", race "+raceId+")";
	}
	
	public Command clone() {
		
		Command clone = new Command();
		clone.setCommandId(this.commandId);
		clone.setVrUserId(this.vrUserId);
		clone.setRaceId(this.raceId);
		clone.setCommandType(this.commandType);
		clone.setValue(this.value);
		clone.setChecksum(this.checksum);
		clone.setR(this.r);
		
		return clone;
	}
	
}
