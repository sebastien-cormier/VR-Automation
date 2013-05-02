package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;

/**
 * ResultSet Extractor for Command Entity
 * @author Sebastien Cormier
 *
 */
public class CommandResultExtractor implements ResultSetExtractor<Command> {

	  @Override
	  public Command extractData(ResultSet rs) throws SQLException {
	    Command command = new Command();
	    command.setCommandId(rs.getInt("commandId"));
	    command.setCommandType(CommandTypeEnum.valueOf(rs.getString("commandType")));
	    command.setVrUserId(rs.getInt("vrUserId"));
	    command.setRaceId(rs.getInt("raceId"));
	    command.setValue(rs.getString("value"));
	    command.setR(rs.getString("r"));
	    command.setChecksum(rs.getString("checksum"));
	    return command;
	  }
	  
	  
}
