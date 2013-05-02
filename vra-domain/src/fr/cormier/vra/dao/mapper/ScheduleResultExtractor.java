package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.Schedule;
import fr.cormier.domain.db.ScheduleStatusEnum;
import fr.cormier.domain.db.ScheduleTestStatusEnum;

/**
 * ResultSet Extractor for Schedule Entity
 * @author Sebastien Cormier
 *
 */

public class ScheduleResultExtractor implements ResultSetExtractor<Schedule> {

	  @Override
	  public Schedule extractData(ResultSet rs) throws SQLException {
		  Schedule schedule = new Schedule();
	    schedule.setScheduleId(rs.getInt("scheduleId"));
	    schedule.setVrUserId(rs.getInt("vrUserId"));
	    schedule.setHeading(rs.getInt("heading"));
	    schedule.setSail(SailEnum.fromString(rs.getString("sail")));
	    schedule.setRaceId(rs.getInt("raceId"));
	    schedule.setStartDate(rs.getTimestamp("startDate"));
	    schedule.setStatus(ScheduleStatusEnum.valueOf(rs.getString("status")));
	    schedule.setTestStatus(ScheduleTestStatusEnum.valueOf(rs.getString("testStatus")));
	    return schedule;
	  }
	  
	  
}
