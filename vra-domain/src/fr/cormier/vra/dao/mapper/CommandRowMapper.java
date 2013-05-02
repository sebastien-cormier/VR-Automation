package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cormier.domain.db.Command;

/**
 * RowMapper for Command Entity
 * @author Sebastien Cormier
 *
 */
public class CommandRowMapper implements RowMapper<Command> {
	
	  @Override
	  public Command mapRow(ResultSet rs, int line) throws SQLException {
		  CommandResultExtractor extractor = new CommandResultExtractor();
	    return extractor.extractData(rs);
	  }

}
