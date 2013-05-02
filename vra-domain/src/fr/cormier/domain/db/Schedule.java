package fr.cormier.domain.db;

import java.util.Date;

import fr.cormier.domain.SailEnum;
import fr.cormier.utils.EqualsUtils;

public class Schedule {
	
	private int scheduleId;
	
	private int vrUserId;
	
	private int raceId;
	
	private int heading;
	
	private SailEnum sail;

	private Date startDate;
	
	private ScheduleStatusEnum status;
	
	private ScheduleTestStatusEnum testStatus;
	

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getVrUserId() {
		return vrUserId;
	}

	public void setVrUserId(int vrUserId) {
		this.vrUserId = vrUserId;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public SailEnum getSail() {
		return sail;
	}

	public void setSail(SailEnum sail) {
		this.sail = sail;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public ScheduleStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ScheduleStatusEnum status) {
		this.status = status;
	}

	public ScheduleTestStatusEnum getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(ScheduleTestStatusEnum testStatus) {
		this.testStatus = testStatus;
	}

	@Override
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof Schedule) ) return false;
		Schedule schedule = (Schedule)object;
		return 
				EqualsUtils.areEqual(this.scheduleId, schedule.scheduleId) &&
				EqualsUtils.areEqual(this.vrUserId, schedule.vrUserId) &&
				EqualsUtils.areEqual(this.raceId, schedule.raceId) &&
				EqualsUtils.areEqual(this.sail, schedule.sail) &&
				EqualsUtils.areEqual(this.heading, schedule.heading) &&
				EqualsUtils.areEqual(this.startDate, schedule.startDate) &&
				EqualsUtils.areEqual(this.status, schedule.status) &&
				EqualsUtils.areEqual(this.testStatus, schedule.testStatus);
	}
	
	public String toString() {
		return "Schedule(id "+scheduleId+", "+startDate+", status "+status+")";
	}
	
	public Schedule clone() {
		Schedule clone = new Schedule();
		clone.setVrUserId(this.vrUserId);
		clone.setRaceId(this.raceId);
		clone.setHeading(this.heading);
		clone.setSail(this.sail);
		clone.setStartDate(this.startDate);
		clone.setStatus(this.status);
		clone.setTestStatus(this.testStatus);
		return clone;
	}
	
}
