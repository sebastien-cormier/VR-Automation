package fr.cormier.domain.db;

import java.util.Date;

import fr.cormier.domain.SailEnum;
import fr.cormier.utils.EqualsUtils;

public class BoatPositionHistory {
		
	private int id;
	
	private int vrUserId;

	private int raceId;
	
	private Date creationDate;
	
	private SailEnum sail;
	
	private int heading;
	
	private double latitude;
	
	private double longitude;
	
	private boolean hasChangedCase;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public SailEnum getSail() {
		return sail;
	}

	public void setSail(SailEnum sail) {
		this.sail = sail;
	}

	public int getHeading() {
		return heading;
	}

	public void setHeading(int heading) {
		this.heading = heading;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isHasChangedCase() {
		return hasChangedCase;
	}

	public void setHasChangedCase(boolean hasCHangedCase) {
		this.hasChangedCase = hasCHangedCase;
	}

	@Override
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof BoatPositionHistory) ) return false;
		BoatPositionHistory boatPositionHistory = (BoatPositionHistory)object;
		return 
				EqualsUtils.areEqual(this.id, boatPositionHistory.id) &&
				EqualsUtils.areEqual(this.vrUserId, boatPositionHistory.vrUserId) &&
				EqualsUtils.areEqual(this.raceId, boatPositionHistory.raceId) &&
				//EqualsUtils.areEqual(this.creationDate, boatPositionHistory.creationDate) &&
				EqualsUtils.areEqual(this.sail, boatPositionHistory.sail) &&
				EqualsUtils.areEqual(this.heading, boatPositionHistory.heading) &&
				EqualsUtils.areEqual(this.latitude, boatPositionHistory.latitude) &&
				EqualsUtils.areEqual(this.longitude, boatPositionHistory.longitude) &&
				EqualsUtils.areEqual(this.hasChangedCase, boatPositionHistory.hasChangedCase);
	}
	
	public String toString() {
		return "BoatPositionHistory(user "+vrUserId+", race "+raceId+" with head "+heading+" and sail "+sail.name()+" located at lat "+latitude+"/"+longitude+")";
	}
	
	public BoatPositionHistory clone() {
		
		BoatPositionHistory clone = new BoatPositionHistory();
		clone.setId(this.id);
		clone.setVrUserId(this.vrUserId);
		clone.setRaceId(this.raceId);
		clone.setCreationDate(this.creationDate);
		clone.setSail(this.sail);
		clone.setHeading(this.heading);
		clone.setLatitude(this.latitude);
		clone.setLongitude(this.longitude);
		clone.setHasChangedCase(this.hasChangedCase);
		return clone;
	}
	
}
