package fr.cormier.vra.service;

import fr.cormier.domain.db.Race;

public interface IRaceService {

	public static final int NO_RACE_FOUND = -1;

	public int addRace(Race race);
	
	public int getRaceId(String name);
	
	public String getRaceName(int raceId);

}
