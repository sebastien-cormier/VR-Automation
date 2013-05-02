package fr.cormier.vra.dao;

import java.util.List;

import fr.cormier.domain.db.Race;

/**
 * DAO class for Schedule table
 * 
 * A UserRace describe the races play by a user and the activated mode for each race
 * 
 * @author Sebastien Cormier
 *
 */
public interface IRaceDao {

	/** List of table columns */
	public static final String COLUMNS = "raceId, name, startDate, status";

	public static final String TABLENAME = "Race";
	
	public int create(final Race race);
	
	public List<Race> selectAll();

}
