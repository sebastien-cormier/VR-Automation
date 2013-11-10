package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import fr.cormier.domain.db.UserRace;

/**
 * ResultSet Extractor for UserRace Entity
 * @author Sebastien Cormier
 *
 */

public class UserRaceResultExtractor implements ResultSetExtractor<UserRace> {

	  @Override
	  public UserRace extractData(ResultSet rs) throws SQLException {
	    UserRace race = new UserRace();
	    race.setVrUserId(rs.getInt("vrUserId"));
	    race.setRaceId(rs.getInt("raceId"));
	    race.setMode(rs.getString("mode"));
	    race.setUserService(rs.getString("userService"));
	    race.setZezoUrlTemplate(rs.getString("zezoUrlTemplate"));
	    return race;
	  }
	  
	  
}
