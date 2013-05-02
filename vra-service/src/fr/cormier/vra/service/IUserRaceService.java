package fr.cormier.vra.service;

import java.util.List;

import fr.cormier.domain.db.UserRace;

public interface IUserRaceService {

	public String getMode(int vrUserId, int raceId);
	
	public String getUserService(int vrUserId, int raceId);

	public List<UserRace> retrieveAllScheduleMode();
	
	public List<UserRace> retrieveAll() ;
	
	public List<UserRace> retrieveAllZezoAutoRouting();
}
