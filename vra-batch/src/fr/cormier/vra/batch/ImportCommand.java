package fr.cormier.vra.batch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.cormier.domain.db.Command;
import fr.cormier.vra.service.ICommandService;
import fr.cormier.vra.service.IRoutingService;
import fr.cormier.vra.service.IScheduleService;
import fr.cormier.vra.service.IUserRaceService;


public class ImportCommand {

	private final static Log logger = LogFactory.getLog(ImportCommand.class);
	
	@Autowired
	private IUserRaceService userRaceService;

	@Autowired
	private IScheduleService scheduleService;

	@Autowired
	private IRoutingService serviceRouting;
	
	@Autowired
	private ICommandService serviceCommand;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		String conf = "spring.xml";
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				conf);

		ImportCommand importCommand = context.getBean(ImportCommand.class);
		
		if( args==null ) {
			System.out.println("Need import file as first argument");
			context.close();
			return;
		}
		String fichier = args[0];
		
		try{
			importCommand.launchImportCommand(fichier);

		}		
		catch (Exception e){
			e.printStackTrace();
		}
	
		context.close();
	}
	
	public void launchImportCommand(String fichier) throws IOException {
		InputStream ips=new FileInputStream(fichier); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String ligne;
		List<Command> commands = new ArrayList<Command>();
		while ((ligne=br.readLine())!=null){
			
			Command command = serviceCommand.parseUrl(ligne);
			if( command!=null ) {
				logger.info("Adding new Command :"+command);
				commands.add(command);
			}
		}
		
		if( commands.size()>0 ) {
			serviceCommand.addCommandList(commands);
		}
		
		br.close(); 		
	}
	

}
