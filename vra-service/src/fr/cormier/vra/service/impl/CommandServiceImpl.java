package fr.cormier.vra.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.domain.db.Race;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.utils.EqualsUtils;
import fr.cormier.utils.Fileutils;
import fr.cormier.utils.HttpUtils;
import fr.cormier.utils.MailUtils;
import fr.cormier.vra.dao.ICommandDao;
import fr.cormier.vra.service.ICommandService;
import fr.cormier.vra.service.IUserRaceService;

@Service("serviceCommande")
public class CommandServiceImpl implements ICommandService {
	
	private final static Log logger = LogFactory.getLog(Command.class);
				
	@Autowired
	private ICommandDao daoCommand;
	
	@Autowired
	private RaceServiceImpl serviceRace;
	
	@Autowired
	private AbstractApplicationContext context;
	
	@Autowired
	private IUserRaceService serviceUserRace;
	
	@Autowired
	private BoatInfoService serviceBoatInfo;
	
	/* duration in second for sleep between each attemp */
	private static final int[] WAIT_FOR_SCHEDULE = {1,2,2,2,2,2};
	private static final int[] WAIT_FOR_ZEZO_ROUTING = {1,2,2,2,2,2,2,2,2,2,2,2,2,4,4,4,4,10,10,10,10,20,20,20};
					
	/**
	 * Create a new command entry if it doesn't exist
	 * if exist, update r & checksum value
	 * @param command entity
	 * @return
	 */
	public int addCommand(Command command) {
		Command sameCommand = daoCommand.select(command.getVrUserId(), command.getRaceId(), command.getCommandType(), command.getValue());
		if( sameCommand==null ) {
		 return daoCommand.create(command);
		} else {
			if( !EqualsUtils.areEqual(command.getChecksum(), sameCommand.getChecksum()) ||
					!EqualsUtils.areEqual(command.getR(), sameCommand.getR()) ) {
				sameCommand.setR(command.getR());
				sameCommand.setChecksum(command.getChecksum());
				daoCommand.updateChecksumAndR(sameCommand);				
			}
			command.setCommandId(sameCommand.getCommandId());
			return sameCommand.getCommandId();
		}
	}
	
	public void addCommandList(List<Command> commands) {
		for (Command command : commands) {
			addCommand(command);
		}
	}
	
	/**
	 * Create a Command domain object from a string url
	 * @param stringUrl
	 * @return
	 */
	public Command parseUrl(String stringUrl) {
		
		if ( stringUrl==null || !stringUrl.startsWith(PROTOCOL)) {
			// WARN : ligne invalide
			return null;
		}
		Command command = new Command();
		URL url;
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e) {
			// WARN : ligne invalide
			return null;
		}
		int raceId = serviceRace.getRaceId(url.getHost());
		if( raceId==RaceServiceImpl.NO_RACE_FOUND ) {
			logger.info("No race found, create new Race : "+url.getHost());
			Race race = new Race();
			race.setName(url.getHost());
			raceId = serviceRace.addRace(race);
		}
		command.setRaceId(raceId);
		
		List<NameValuePair> urlArgs= URLEncodedUtils.parse(stringUrl, Charset.defaultCharset());

		for (NameValuePair nameValuePair : urlArgs) {

			setCommandValue(nameValuePair, command);
	        
		}
		return command;
	}
	
	public String generateStringUrl(Command command) {
		
		StringBuffer stringUrl = new StringBuffer();
		stringUrl.append(PROTOCOL);
		stringUrl.append(serviceRace.getRaceName(command.getRaceId()));
		stringUrl.append(VR_UPDATE_SERVICE);
		
		stringUrl.append(PARAM_SEPARATOR);
		stringUrl.append(PARAM_USER_ID+"="+command.getVrUserId());
		
		/*HACK pour la course clipper : pas de boat_id*/
		if( command.getRaceId()!=8 && command.getRaceId()!=11  && command.getRaceId()!=9 ) {
			stringUrl.append(PARAM_SEPARATOR);
			stringUrl.append(PARAM_BOAT_ID+"="+command.getVrUserId());
		}
		
		stringUrl.append(PARAM_SEPARATOR);
		if( CommandTypeEnum.HEADING.equals(command.getCommandType()) ) {
			stringUrl.append(PARAM_CAP+"="+command.getValue());
		} else {
			stringUrl.append(PARAM_SAIL+"="+command.getValue());
		}

		stringUrl.append(PARAM_SEPARATOR);
		stringUrl.append(PARAM_R+"="+command.getR());

		stringUrl.append(PARAM_SEPARATOR);
		stringUrl.append(PARAM_CHECKSUM+"="+command.getChecksum());

		
		return stringUrl.toString();
	}
	
	private static void setCommandValue(NameValuePair nameValuePair, Command command) {
		if( nameValuePair==null || command==null || nameValuePair.getName()==null ) {
			return;
		}
		if( nameValuePair.getName().indexOf('?')>0 ) {
			return;
		}
		if( nameValuePair.getName().equals(PARAM_R)) {
			command.setR(nameValuePair.getValue());
		} else if( nameValuePair.getName().equals(PARAM_CHECKSUM) ) {
			command.setChecksum(nameValuePair.getValue());
		} else if( nameValuePair.getName().equals(PARAM_USER_ID) ) {
			command.setVrUserId(Integer.parseInt(nameValuePair.getValue()));
		} else if( nameValuePair.getName().equals(PARAM_CAP) ) {
			command.setCommandType(CommandTypeEnum.HEADING);
			command.setValue(nameValuePair.getValue());
		} else if( nameValuePair.getName().equals(PARAM_SAIL) ) {
			command.setCommandType(CommandTypeEnum.SAIL);
			command.setValue(nameValuePair.getValue());			
		}
	}

	public Command getCommand(int commandId) {

		return daoCommand.selectByKey(commandId);
	}

	public boolean runCommand(int vrUserId, int raceId, CommandTypeEnum commandType,
			String heading) {
		
		Command command = daoCommand.findByValues(vrUserId, raceId, String.valueOf(commandType),
				heading);
		
		if( command==null ) {
			return false;
			
		} else {
			
			if( isTestMode() ) {
				logger.info("MODE TEST : simulate command "+command.getCommandId()+" for url : "+generateStringUrl(command));
				
			} else {
				try {
					String response = HttpUtils.wget(generateStringUrl(command));
					if( response.contains("state=KO")) logger.info("KKKKOOOOOO\n"+generateStringUrl(command));
					if( response.contains("state=OK")) logger.info("OOOOKKKKK");
					
					String rMode = serviceUserRace.getMode(vrUserId, raceId);
					
					int[] wait = {1};
					if( "SCHEDULE".equals(rMode) ) {
						wait = WAIT_FOR_SCHEDULE;
					}
					else if( "ZEZO_ROUTING".equals(rMode) ) {
						wait = WAIT_FOR_ZEZO_ROUTING;
					}

					
					if( !checkCommand(command, wait) ) {
						logger.error("Abording command, sending error message...");
						logger.error("URL:"+generateStringUrl(command));
						BoatsPositionWrapper boatsPositionWrapper = serviceBoatInfo.retrieveBoatPosition(vrUserId, raceId);
						
						MailUtils.sendEmail("sebastien.cormier@gmail.com", 
								"VRA - Error when execution command "+command.getCommandType()+ " with value "+command.getValue(), 
								"Date : "+new Date()+"\n\n" +
								"Race : "+serviceRace.getRaceName(raceId)+"\n" +
								"Routing : "+rMode+"\n\n" +
								"Current Heading : "+boatsPositionWrapper.getHeading()+"\n"+
								"Current Sail : "+SailEnum.getStringValue(boatsPositionWrapper.getSail())+"\n"+
								"Current State : "+boatsPositionWrapper.getState()+"\n\n" +
								"Command to apply : "+command.toString()
								);
					}
					
				} catch (IOException e) {
					logger.error("Could not execute command "+command.getCommandId()+" for url : "+generateStringUrl(command));
					return false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
	}
	
	/**
	 * Check if command has been applied and do another attempt if necessary
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean checkCommand(Command command, int[] wait) throws IOException, InterruptedException {
		
		for(int i=0; i<wait.length; i++) {

			BoatsPositionWrapper boatPos = serviceBoatInfo.retrieveBoatPosition(command.getVrUserId(), command.getRaceId());
			
			if( CommandTypeEnum.HEADING.equals(command.getCommandType())) {
				
				if( boatPos.getHeading()==Integer.parseInt(command.getValue()) ) {
					logger.info("Heading on "+command.getValue()+" is confirmed");
					return true;
				} 
			} else if( CommandTypeEnum.SAIL.equals(command.getCommandType())) {
				
				if( boatPos.getSail()==Integer.parseInt(command.getValue()) ) {
					logger.info("Sail ("+command.getValue()+") is confirmed");
					return true;
				}
			}
			
			logger.info("  -- waiting "+wait[i]+" sec for next retry ....");
			Thread.sleep(wait[i]*1000);
			
			logger.info("  -- Attempt number "+(i+2)+"...");
			String response = HttpUtils.wget(generateStringUrl(command));
			if( response.contains("state=KO")) logger.info("KKKKOOOOOO");
			if( response.contains("state=OK")) logger.info("OOOOKKKKK");
			
		} 
		return false;
	
	}

	private boolean isTestMode() {
		String activeProfiles[] = context.getEnvironment().getActiveProfiles();
		if( activeProfiles!=null ) {
			for(int i=0; i<activeProfiles.length; i++) {
				if( activeProfiles[i].equals("TEST")) {
					return true;
				}
			}
		}
		return false;
	}

	public List<Command> getCommands(int vrUserId, int raceId) {
		return daoCommand.select(vrUserId, raceId);
	}

	public void generateCommandFile(String targetDir, Command command) {
		
		String fileName = getCommandFileName(targetDir, command);
		
		String content = "CD %DROPBOX_ROOT%\\Documents\\VirtualRegatta\\logs\n"+
				"%DROPBOX_ROOT%\\utils\\wget\\wget.exe -b --server-response --spider --user-agent=\"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11\" ";
		
		Fileutils.writeFile(fileName, content+"\""+generateStringUrl(command)+"\"");
		
	}
	
	private String getCommandFileName(String targetDir, Command command) {
		String raceName = serviceRace.getRaceName(command.getRaceId());
		String value = command.getValue();
		if( CommandTypeEnum.HEADING.equals(command.getCommandType()) ) {
			if( value.length()==1 ) {
				value = "00"+value;
			} else if(value.length()==2){
				value = "0"+value;
			}
		} else if( CommandTypeEnum.SAIL.equals(command.getCommandType()) ) {
			value = SailEnum.getStringValue(Integer.parseInt(value));
		}
		return targetDir+"/"+raceName+"_"+command.getVrUserId()+"_"+command.getCommandType()+"_"+value+".bat";
	}

	@Override
	public Map<CommandTypeEnum, List<String>> retrieveMissingCommands(int userId, int raceId) {
		
		Map<String,Command> headingCommands = new HashMap<String,Command>();
		Map<String,Command> sailCommands = new HashMap<String,Command>();
		
		List<Command> allCommands = daoCommand.select(userId, raceId);
		
		for (Command command : allCommands) {
			if( CommandTypeEnum.HEADING.equals(command.getCommandType()) ) {
				headingCommands.put(command.getValue(), command);
			} else if( CommandTypeEnum.SAIL.equals(command.getCommandType()) ) {
				sailCommands.put(command.getValue(), command);
			}
		}
		
		Map<CommandTypeEnum, List<String>> result = new HashMap<CommandTypeEnum, List<String>>();
		
		List<String> missingHeading = new ArrayList<String>();
		for(int heading=0; heading<360; heading++) {
			if( headingCommands.get(String.valueOf(heading))==null ) {
				missingHeading.add(String.valueOf(heading));
			}
		}
		result.put(CommandTypeEnum.HEADING, missingHeading);
		
		List<String> missingSails = new ArrayList<String>();
		for(SailEnum sail : SailEnum.getAllSails() ) {
			if( sailCommands.get(String.valueOf(sail.getValue()))==null ) {
				missingSails.add(sail.getStringValue());
			}
		}
		result.put(CommandTypeEnum.SAIL, missingSails);
		
		return result;
	}
	
}
