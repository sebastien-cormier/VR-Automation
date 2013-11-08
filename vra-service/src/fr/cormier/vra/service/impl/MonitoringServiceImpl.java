package fr.cormier.vra.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.vra.service.IMonitoringService;

@Service("serviceMonitoring")
public class MonitoringServiceImpl implements IMonitoringService {

	private final static Log logger = LogFactory.getLog(MonitoringServiceImpl.class);
	
	@Autowired
	private BoatInfoService serviceBoatInfo = null;

	@Override
	public void launchMonitoringJob(List<UserRace> userRaces) {
		
		logger.debug("launch Monitoring Job");
		
		for (UserRace userRace : userRaces) {
			
			// Retrieve last position, new position and save new position
			BoatsPositionWrapper boatsPositionWrapper = serviceBoatInfo.retrieveBoatPosition(userRace.getVrUserId(), userRace.getRaceId());

			// If same square and routing, skipping monitoring
			
			// Check if need to change sail
			
			// Check speed thresold
			
			
		}
		
	}
	

	
}
