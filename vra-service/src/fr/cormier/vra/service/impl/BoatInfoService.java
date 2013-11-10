package fr.cormier.vra.service.impl;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.domain.xml.getuser.Position;
import fr.cormier.domain.xml.getuser.Result;
import fr.cormier.domain.xml.getuser.User;
import fr.cormier.utils.MailUtils;
import fr.cormier.vra.service.IBoatInfoService;
import fr.cormier.vra.service.IRaceService;
import fr.cormier.vra.service.IUserRaceService;

@Service("serviceBoatInfo")
public class BoatInfoService implements IBoatInfoService {

	private final static Log logger = LogFactory.getLog(BoatInfoService.class);
	
	@Autowired
	private IUserRaceService serviceUserRace = null;
	
	@Autowired
	private IRaceService serviceRace = null;
	
	private boolean testMode = false;
			
	public BoatInfoService() {
		testMode = false;
	}
	
	

	public boolean isTestMode() {
		return testMode;
	}



	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}


	
	public BoatsPositionWrapper retrieveBoatPosition(int vrUserId, int raceId) {
		
		if(testMode) {
			return new BoatsPositionWrapper(getFakeResult());
		}
		try {
			Serializer serializer = new Persister();
			
			String userService = serviceUserRace.getUserService(vrUserId, raceId);
			
	        URL gotoUrl = new URL(userService);
	        
	        
	        InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
			Result result = serializer.read(Result.class, isr);			
			isr.close();
			return new BoatsPositionWrapper(result);
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Can not retrieve boat position for vruser "+vrUserId+", race id "+raceId);
			MailUtils.sendEmail("sebastien.cormier@gmail.com", "VRA ERROR - Can not retrieve boat position !!!", 
					"Date : "+new Date()+"\n"+
					"Error : Can not retrieve boat position for user "+vrUserId+" on race "+serviceRace.getRaceName(raceId)+"\n\n\n"+
					e.getMessage());
			return null;
		}
	}
	
	private Result getFakeResult() {
		logger.warn("!!! TEST MODE !!! => Generating fake result ....");

		Result result = new Result();
		result.setUser(new User());
		result.getUser().setPosition(new Position());
		result.getUser().getPosition().setCap(90);
		result.getUser().getPosition().setLatitude("-41.833988566");
		result.getUser().getPosition().setLongitude("174.645345061");
		
		return result;
	}

}
