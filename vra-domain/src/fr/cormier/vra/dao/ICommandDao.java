package fr.cormier.vra.dao;

import java.util.List;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;

/**
 * DAO class for Command table
 * 
 * A command is a description how to launch a command to retrieve/modify data on
 * Virtual Regatta
 * 
 * @author Sebastien Cormier
 * 
 */
public interface ICommandDao {

	public static final String COLUMNS = "commandId, commandType, vrUserId, raceId, value, r, checksum";
	public static final String TABLENAME = "Command";

	public int create(final Command command);

	public Command select(int userId, int raceId, CommandTypeEnum commandType,
			String value);
	
	public Command selectByKey(int commandId);

	public int updateChecksumAndR(Command command);

	public Command findByValues(int vrUserId, int raceId,
			String commandTypeHeading, String heading);

	public List<Command> select(int vrUserId, int raceId);

}
