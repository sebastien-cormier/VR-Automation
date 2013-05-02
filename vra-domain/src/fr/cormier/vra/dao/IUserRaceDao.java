package fr.cormier.vra.dao;

import java.util.List;

import fr.cormier.domain.db.UserRace;

/**
 * DAO for UserRace table
 * 
 * @author Sebastien Cormier
 * 
 */
public interface IUserRaceDao {

	public static final String COLUMNS = "vrUserId, raceId, mode, userService";

	public static final String TABLENAME = "UserRace";

	public int create(final UserRace userRace);
	
	public List<UserRace> selectAll();
	
	public UserRace selectByKey(int vrUserId, int raceId);
	
	public List<UserRace> selectAllSchedule();

	public List<UserRace> selectAllZezoAutoRouting();

}
