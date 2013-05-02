package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cormier.domain.db.BoatPositionHistory;

/**
 * RowMapper for Command Entity
 * @author Sebastien Cormier
 *
 */
public class BoatPositionHistoryRowMapper implements RowMapper<BoatPositionHistory> {
	
	  @Override
	  public BoatPositionHistory mapRow(ResultSet rs, int line) throws SQLException {
		  BoatPositionHistoryResultExtractor extractor = new  BoatPositionHistoryResultExtractor();
	    return extractor.extractData(rs);
	  }

}
