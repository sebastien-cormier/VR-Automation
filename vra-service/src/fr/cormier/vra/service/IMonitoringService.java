package fr.cormier.vra.service;

import java.util.List;

import fr.cormier.domain.db.UserRace;


public interface IMonitoringService {

	public void launchMonitoringJob(List<UserRace> userRace);

}
