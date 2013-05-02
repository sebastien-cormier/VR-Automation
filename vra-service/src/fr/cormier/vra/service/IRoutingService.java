package fr.cormier.vra.service;

import java.util.List;

import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.external.Position;
import fr.cormier.domain.external.RoutingCommand;

public interface IRoutingService {
	
	public void setTestMode(boolean testMode);
	
	public RoutingCommand retrieveCurrentRoutingCommand(Position position);

	public void setMockHtml(String string);

	public void processZezoRouting(List<UserRace> userRaces);

}
