package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import fr.cormier.domain.db.Race;

/**
 * ResultSet Extractor for Race Entity
 * @author Sebastien Cormier
 *
 */
public class RaceResultExtractor implements ResultSetExtractor<Race> {

	  @Override
	  public Race extractData(ResultSet rs) throws SQLException {
	    Race race = new Race();
	    race.setRaceId(rs.getInt("raceId"));
	    race.setName(rs.getString("name"));
	    race.setStartDate(rs.getTimestamp("startDate"));
	    race.setStatus(rs.getString("status"));
	    return race;
	  }
	  
	  
}
