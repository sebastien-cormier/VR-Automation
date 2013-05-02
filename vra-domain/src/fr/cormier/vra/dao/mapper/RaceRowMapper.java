package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cormier.domain.db.Race;

/**
 * RowMapper for Race Entity
 * @author Sebastien Cormier
 *
 */
public class RaceRowMapper implements RowMapper<Race> {
	
	  @Override
	  public Race mapRow(ResultSet rs, int line) throws SQLException {
		  RaceResultExtractor extractor = new RaceResultExtractor();
	    return extractor.extractData(rs);
	  }

}
