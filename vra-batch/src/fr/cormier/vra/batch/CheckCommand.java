package fr.cormier.vra.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.domain.db.UserRace;
import fr.cormier.domain.xml.getuser.BoatsPositionWrapper;
import fr.cormier.vra.service.IBoatInfoService;
import fr.cormier.vra.service.ICommandService;
import fr.cormier.vra.service.IRoutingService;
import fr.cormier.vra.service.IScheduleService;
import fr.cormier.vra.service.IUserRaceService;

public class CheckCommand {
	
	private final static Log logger = LogFactory.getLog(Launcher.class);


	@Autowired
	private IUserRaceService userRaceService;

	@Autowired
	private ICommandService commandService;

	@Autowired
	private IBoatInfoService boatInfoService;

	public CheckCommand() {

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		String conf = "spring.xml";
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				conf);
		ConfigurableEnvironment configurableEnvironment = new StandardEnvironment();
		
		boolean testMode = false;
		
		if (args != null && args.length > 0) {
			if ("-test".equals(args[0])) {
				configurableEnvironment.setActiveProfiles("TEST");
				testMode = true;
			} else {
				configurableEnvironment.setActiveProfiles("PROD");
			}
		} else {
			configurableEnvironment.setActiveProfiles("PROD");
		}
		context.setEnvironment(configurableEnvironment);

		PropertyConfigurator.configure("log4j.properties");
		
		int userId = 1557492;
		int raceId = 3;
	

		CheckCommand checkCommand = context.getBean("checkCommand", CheckCommand.class);

		checkCommand.checkCommands(testMode, userId, raceId);
		
			
		
		
	}

	private void checkCommands(boolean testMode, int userId, int raceId) throws InterruptedException {
		
		logger.info("Starting check commands for user id "+userId+" on race id "+raceId);
		
		List<Command> commands = commandService.getCommands(userId, raceId);
		logger.info("Found "+commands.size()+" commands");
		
		BoatsPositionWrapper initialBoatPosition = boatInfoService.retrieveBoatPosition(userId, raceId);
		logger.info("Boat is initially on head "+initialBoatPosition.getHeading()+" with sail "+initialBoatPosition.getSail());
		for (Command command : commands) {

			commandService.runCommand(command.getVrUserId(), command.getRaceId(), command.getCommandType(), command.getValue());
			
			Thread.sleep(1000);

			if( CommandTypeEnum.HEADING.equals(command.getCommandType()) ) {
				checkHeadingCommand(command);
				
			} else if(CommandTypeEnum.HEADING.equals(command.getCommandType()) ) {
				checkSailCommand(command);
			}

		}
		
		logger.info("Restoring boat position to head "+initialBoatPosition.getHeading()+" and sail "+initialBoatPosition.getSail());
		
		commandService.runCommand(userId, raceId, CommandTypeEnum.HEADING, String.valueOf(initialBoatPosition.getHeading()));
		commandService.runCommand(userId, raceId, CommandTypeEnum.SAIL, String.valueOf(initialBoatPosition.getSail()));
		
	}


	private void checkSailCommand(Command command) throws InterruptedException {
		
		
		BoatsPositionWrapper boatsPositionWrapper = boatInfoService.retrieveBoatPosition(command.getVrUserId(), command.getRaceId());
		if( boatsPositionWrapper.getSail()==Integer.parseInt(command.getValue()) ) {
			logger.info("  <OK> for Command SAIL " + SailEnum.fromInt(Integer.parseInt(command.getValue())) );
		} else {
			logger.error("!!<KO> for Command SAIL " + SailEnum.fromInt(Integer.parseInt(command.getValue())) );
			
			logger.info(" Retriing same command.... ");
			commandService.runCommand(command.getVrUserId(), command.getRaceId(), command.getCommandType(), command.getValue());
			
			Thread.sleep(1000);
			checkSailCommand(command);
		
		}
		
	}

	private void checkHeadingCommand(Command command) throws InterruptedException  {
		
		BoatsPositionWrapper boatsPositionWrapper = boatInfoService.retrieveBoatPosition(command.getVrUserId(), command.getRaceId());
		if( boatsPositionWrapper.getHeading()==Integer.parseInt(command.getValue()) ) {
			logger.info("  <OK> for Command HEADING " + command.getValue() );
		} else {
			logger.error("!!<KO> for Command HEADING " + command.getValue() );
			logger.info(" Retriing same command.... ");
			commandService.runCommand(command.getVrUserId(), command.getRaceId(), command.getCommandType(), command.getValue());
			
			Thread.sleep(1000);
			checkHeadingCommand(command);
		}

		
		
	}
}