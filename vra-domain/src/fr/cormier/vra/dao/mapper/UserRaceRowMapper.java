package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cormier.domain.db.UserRace;

/**
 * RowMapper for UserRace Entity
 * @author Sebastien Cormier
 *
 */
public class UserRaceRowMapper implements RowMapper<UserRace> {
	
	  @Override
	  public UserRace mapRow(ResultSet rs, int line) throws SQLException {
		  UserRaceResultExtractor extractor = new UserRaceResultExtractor();
	    return extractor.extractData(rs);
	  }

}
