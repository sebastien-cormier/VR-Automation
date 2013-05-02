package fr.cormier.vra.batch;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import fr.cormier.domain.db.Command;
import fr.cormier.vra.service.impl.CommandServiceImpl;


public class GenerateCommandFiles {


	private final static Log logger = LogFactory.getLog(GenerateCommandFiles.class);
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		if( args==null || args.length<3) {
			System.out.println("Usage : GenerateCommandFiles [vrUserId] [raceId] [target directory]");
			return;
		}
		
		logger.info("Starting command file generation for vrUserId");
		
		
		CommandServiceImpl commandService = new CommandServiceImpl();
		BasicConfigurator.configure();
		
		
		try{
			
			int vrUserId = Integer.parseInt(args[0]);
			int raceId = Integer.parseInt(args[1]);
			String targetDir = args[2];
			
			List<Command> commands = commandService.getCommands(vrUserId, raceId);
			
			for (Command command : commands) {
				commandService.generateCommandFile(targetDir,command);
			}
			
		}		
		catch (Exception e){
			e.printStackTrace();
		}
	

	}
	

}
