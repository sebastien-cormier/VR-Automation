package fr.cormier.domain.db;

import java.util.Date;

import fr.cormier.utils.EqualsUtils;

public class Race {
		
	private int raceId;
	
	private String name;
	
	private Date startDate;
	
	private RaceStatusEnum status;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public RaceStatusEnum getStatus() {
		return status;
	}

	public void setStatus(RaceStatusEnum status) {
		this.status = status;
	}
	
	public void setStatus(String status) {
		this.status = RaceStatusEnum.valueOf(status);
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof Race) ) return false;
		Race race = (Race)object;
		return 
				EqualsUtils.areEqual(this.raceId, race.raceId) &&
				EqualsUtils.areEqual(this.name, race.name) &&
				EqualsUtils.areEqual(this.startDate, race.startDate) &&
				EqualsUtils.areEqual(this.status, race.status);
	}
	
	public String toString() {
		return "Race("+raceId+","+name+")";
	}
	
	public Race clone() {
		
		Race clone = new Race();
		clone.setRaceId(this.raceId);
		clone.setName(this.name);
		clone.setStartDate(this.startDate);
		clone.setStatus(this.status);
		
		return clone;
	}
	
}
