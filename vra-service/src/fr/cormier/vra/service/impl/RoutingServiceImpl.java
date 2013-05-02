package fr.cormier.vra.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.external.Position;
import fr.cormier.domain.external.RoutingCommand;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.vra.service.IRoutingService;

@Service("serviceRouting")
public class RoutingServiceImpl implements IRoutingService {

	private final static Log logger = LogFactory.getLog(RoutingServiceImpl.class);

	private static final String LINE_FIND_TEMPLATE_START = "	onmouseover=\"updi(event";

	private String mockHtml;
	
	private static final String HEADING_FIND_TEMPLATE_START = "<b>Heading:</b> ";
	private static final String HEADING_FIND_TEMPLATE_END = "&deg;";
	
	private static final String SAIL_FIND_TEMPLATE_START = "<b>Sail:</b> ";
	private static final String SAIL_FIND_TEMPLATE_END = "<br><b>Boat";
	
	//private static final String ZEZO_URL_TEMPLATE = "http://sail.zezo.org/austral/chart.pl?lat=@LATITUDE&lon=@LONGITUDE&o=0&wind=0&tlon=-66.9818&tlat=-56.0912&clon=-153.0625&clat=-48.875";
	private static final String ZEZO_URL_TEMPLATE = "http://sail.zezo.org/fr/chart.pl?lat=@LATITUDE&lon=@LONGITUDE&o=0&wind=0clon=6.71875&clat=43.28125";
	
	private boolean testMode = false;
		
	@Autowired
	private BoatInfoService serviceBoatInfo = null;
	
	@Autowired
	private CommandServiceImpl commandService = null;
	
	
	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	@Override
	public RoutingCommand retrieveCurrentRoutingCommand(Position position) {

		String htmlDoc = null;
		
		if( mockHtml!=null ) {
			htmlDoc = mockHtml;
		} else {
			htmlDoc = initDocFromZezo(position);
			if( htmlDoc==null ) {
				return null;
			}
		}
		
		return parseHtmlDoc(htmlDoc);
	}

	private String initDocFromZezo(Position position) {
		
		if( mockHtml!=null ) {
			return mockHtml;
		}
		String strURL = ZEZO_URL_TEMPLATE.replaceAll("@LATITUDE", String.valueOf(position.getLatitude())).replaceAll("@LONGITUDE", String.valueOf(position.getLongitude()));
		logger.info("Contactiong ZEZO on "+strURL);
		try {
			InputStream in = new URL( strURL ).openStream();
			return convertStreamToString(in);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	private RoutingCommand parseHtmlDoc(String htmlDoc) {
		String line = retrieveFirstLine(htmlDoc);
		if( line!=null ) {
			RoutingCommand routingCommand = new RoutingCommand();
			routingCommand.setHeading( parseHeading(line) );
			routingCommand.setSail(parseSail(line));
			return routingCommand;
		}
		return null;
	}

	protected SailEnum parseSail(String line) {
		int start = line.indexOf(SAIL_FIND_TEMPLATE_START) + SAIL_FIND_TEMPLATE_START.length();
		int end = line.indexOf(SAIL_FIND_TEMPLATE_END, start);
		String strSail = line.substring(start,end);
		return SailEnum.fromString(strSail);
	}

	protected int parseHeading(String line) {
		int start = line.indexOf(HEADING_FIND_TEMPLATE_START) + HEADING_FIND_TEMPLATE_START.length();
		int end = line.indexOf(HEADING_FIND_TEMPLATE_END, start);
		String strHeading = line.substring(start,end);
		return Integer.parseInt(strHeading);
	}

	private String retrieveFirstLine(String htmlDoc) {
		String[] lines = htmlDoc.split("\n");
		for(int i=0; i<lines.length; i++) {
			if( lines[i].startsWith(LINE_FIND_TEMPLATE_START) ) {
				return lines[i];
			}
		}
		return null;
	}

	@Override
	public void setMockHtml(String mockHtmlPath) {
		InputStream is = getClass().getResourceAsStream(mockHtmlPath);
		mockHtml = RoutingServiceImpl.convertStreamToString(is);
	}

	private static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	@Override
	public void processZezoRouting(List<UserRace> userRaces) {
		
		if( testMode ) {
			this.setMockHtml("/test_zezo_03.htm");
			serviceBoatInfo.setTestMode(true);
		}
		
		for (UserRace userRace : userRaces) {
			
			logger.info("Processing zezo auto routing for vr user id "+userRace.getVrUserId());
			
			BoatsPositionWrapper boatsPositionWrapper = serviceBoatInfo.retrieveBoatPosition(userRace.getVrUserId(), userRace.getRaceId());
			Position position = new Position();
			position.setLatitude(boatsPositionWrapper.getLatitude());
			position.setLongitude(boatsPositionWrapper.getLongitude());
			RoutingCommand routingCommand = this.retrieveCurrentRoutingCommand(position);
			if( routingCommand==null ) {
				logger.error("Can not retrieve routing, skipping iteration...");
				continue;
			}

			if(boatsPositionWrapper.getHeading()==routingCommand.getHeading() ) {
				logger.info("Heading didn't change, continue on head "+boatsPositionWrapper.getHeading());
			} else {
				logger.info("\n\n -- Changing heading from "+boatsPositionWrapper.getHeading()+" to "+routingCommand.getHeading()+" -- \n");
				commandService.runCommand(userRace.getVrUserId(), userRace.getRaceId(), CommandTypeEnum.HEADING, String.valueOf(routingCommand.getHeading()));
			}
			if( SailEnum.fromInt(boatsPositionWrapper.getSail())==routingCommand.getSail() ) {
				logger.info("Sail didn't change, continue with sail "+routingCommand.getSail());
			} else {
				logger.info("\n\n -- Changing sail from "+SailEnum.fromInt(boatsPositionWrapper.getSail())+" to "+routingCommand.getSail()+" -- \n");
				commandService.runCommand(userRace.getVrUserId(), userRace.getRaceId(), CommandTypeEnum.SAIL, routingCommand.getSail().getStringValue());				
			}
			
		}
		
	}
	
}