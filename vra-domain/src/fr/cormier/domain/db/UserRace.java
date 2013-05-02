package fr.cormier.domain.db;

import fr.cormier.utils.EqualsUtils;

public class UserRace {
		
	public static final String MODE_NONE = "NONE";
	
	public static final String MODE_SCHEDULE = "SCHEDULE";
	
	public static final String MODE_WAYPOINT = "WAYPOINT";
	
	public static final String MODE_SPEEDREG = "SPEEDREG";

	public static String MODE_ZEZO_AUTO_ROUTING = "ZEZO_ROUTING";
	
	private int vrUserId;
	
	private int raceId;
	
	private String mode;

	private String userService;
	
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUserService() {
		return userService;
	}

	public void setUserService(String userService) {
		this.userService = userService;
	}

	@Override
	public boolean equals(Object object) {
		if( this==object ) return true;
		if( !(object instanceof UserRace) ) return false;
		UserRace userRace = (UserRace)object;
		return 
				EqualsUtils.areEqual(this.vrUserId, userRace.vrUserId) &&
				EqualsUtils.areEqual(this.raceId, userRace.raceId) &&
				EqualsUtils.areEqual(this.mode, userRace.mode) &&
				EqualsUtils.areEqual(this.userService, userRace.userService);
	}
	
	public String toString() {
		return "UserRace("+vrUserId+","+raceId+","+mode+")";
	}
	
	public UserRace clone() {
		UserRace clone = new UserRace();
		clone.setVrUserId(this.vrUserId);
		clone.setRaceId(this.raceId);
		clone.setMode(this.mode);
		clone.setUserService(this.userService);
		return clone;
	}
	
}
