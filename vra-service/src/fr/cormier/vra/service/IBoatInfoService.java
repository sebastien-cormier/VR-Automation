package fr.cormier.vra.service;

import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;

public interface IBoatInfoService {
	
	public BoatsPositionWrapper retrieveBoatPosition(int vrUserId, int raceId);

}
