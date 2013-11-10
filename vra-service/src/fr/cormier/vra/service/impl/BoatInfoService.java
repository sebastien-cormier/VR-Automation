package fr.cormier.vra.service.impl;

import java.io.InputStreamReader;
import java.net.URL;

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
import fr.cormier.vra.service.IBoatInfoService;

@Service("serviceBoatInfo")
public class BoatInfoService implements IBoatInfoService {

	private final static Log logger = LogFactory.getLog(BoatInfoService.class);
	
	@Autowired
	private UserRaceServiceImpl serviceUserRace = null;
	
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



	public boolean checkCap(int cap) throws Exception {
		Result result;
		if( testMode ) {
			result = getFakeResult();
		} else {
			Serializer serializer = new Persister();
			URL gotoUrl = new URL("http://frozen.virtualregatta.com/core/Service/ServiceCaller.php?service=GetFriendsList&id_user=1557492&checksum=e66559616ae215e6848821cb5f54635b00b20417");
			InputStreamReader isr = new InputStreamReader(gotoUrl.openStream());
			result = serializer.read(Result.class, isr);
			isr.close();
		}
		
		return cap==result.getUser().getPosition().getCap();
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
