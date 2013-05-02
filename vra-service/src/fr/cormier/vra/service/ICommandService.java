package fr.cormier.vra.service;

import java.util.List;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;

public interface ICommandService {
			
	public static final String PROTOCOL = "http://";
	
	public static final String VR_UPDATE_SERVICE = "/core/Service/ServiceCaller.php?service=Update";
	
	public static final char PARAM_SEPARATOR = '&';
	
	public static final String PARAM_R = "r";
	
	public static final String PARAM_CHECKSUM = "checksum";
	
	public static final String PARAM_CAP = "cap";
	
	public static final String PARAM_SAIL = "voile";
	
	public static final String PARAM_USER_ID = "id_user";

	public int addCommand(Command command);
	
	public void addCommandList(List<Command> commands);
	
	public Command parseUrl(String stringUrl);
	
	public String generateStringUrl(Command command);
		
	public Command getCommand(int commandId);
	
	public boolean runCommand(int vrUserId, int raceId, CommandTypeEnum commandType,
			String heading);

	public List<Command> getCommands(int vrUserId, int raceId);

	public void generateCommandFile(String targetDir, Command command);
	
}
