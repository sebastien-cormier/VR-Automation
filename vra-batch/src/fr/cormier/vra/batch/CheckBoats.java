package fr.cormier.vra.batch;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.vra.service.impl.BoatInfoService;
import fr.cormier.vra.service.impl.UserRaceServiceImpl;



public class CheckBoats {


	private final static Log logger = LogFactory.getLog(CheckBoats.class);
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		PropertyConfigurator.configure("resources/log4j.properties");
		
		BoatInfoService boatInfoService = new BoatInfoService();
		UserRaceServiceImpl userRaceService = new UserRaceServiceImpl();
		
		List<UserRace> userRaces = userRaceService.retrieveAll();
		for (UserRace userRace : userRaces) {
			logger.info("Retrieving position for user "+userRace.getVrUserId()+" in race id "+userRace.getRaceId()+" ... ");
			BoatsPositionWrapper boatPosition = boatInfoService.retrieveBoatPosition(userRace.getVrUserId(), userRace.getRaceId());
			if( boatPosition==null ) {
				logger.warn("No position found !!!");
			} else {
				logger.info("Heading is "+boatPosition.getHeading()+" with "+SailEnum.fromInt(boatPosition.getSail()));
			}
		}
		
		boatInfoService.checkCap(17);
		
	

	}
	

}
