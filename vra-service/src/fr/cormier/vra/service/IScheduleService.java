package fr.cormier.vra.service;

import java.util.List;

import fr.cormier.domain.db.UserRace;


public interface IScheduleService {

	public void processScheduleRotation(List<UserRace> userRaces);
	
	public void checkAllCurrentSchedule(List<UserRace> userRaces);
}
