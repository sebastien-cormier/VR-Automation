package fr.cormier.vra.dao;

import fr.cormier.domain.db.BoatPositionHistory;

/**
 * DAO class for Command table
 * 
 * A command is a description how to launch a command to retrieve/modify data on
 * Virtual Regatta
 * 
 * @author Sebastien Cormier
 * 
 */
public interface IBoatPositionHistoryDao {

	public static final String COLUMNS = "id,vrUserId,raceId,creationDate,sail,heading,latitude,longitude,hasChangedCase";
	public static final String TABLENAME = "BoatPositionHistory";

	public int create(final BoatPositionHistory boatPositionHistory);
	
	public BoatPositionHistory selectLastPosition(int testVrUserId,
			int testRaceId);


}
