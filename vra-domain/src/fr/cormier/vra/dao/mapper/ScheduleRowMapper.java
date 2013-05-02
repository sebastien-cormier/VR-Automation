package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fr.cormier.domain.db.Schedule;

/**
 * RowMapper for Schedule Entity
 * @author Sebastien Cormier
 *
 */
public class ScheduleRowMapper implements RowMapper<Schedule> {
	
	  @Override
	  public Schedule mapRow(ResultSet rs, int line) throws SQLException {
		  ScheduleResultExtractor extractor = new ScheduleResultExtractor();
	    return extractor.extractData(rs);
	  }

}
