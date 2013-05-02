package fr.cormier.vra.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.BoatPositionHistory;

/**
 * ResultSet Extractor for Command Entity
 * @author Sebastien Cormier
 *
 */
public class BoatPositionHistoryResultExtractor implements ResultSetExtractor<BoatPositionHistory> {

	  @Override
	  public BoatPositionHistory extractData(ResultSet rs) throws SQLException {
		  BoatPositionHistory boatPositionHistory = new BoatPositionHistory();
	    boatPositionHistory.setId(rs.getInt("id"));
	    boatPositionHistory.setVrUserId(rs.getInt("vrUserId"));
	    boatPositionHistory.setRaceId(rs.getInt("raceId"));
	    boatPositionHistory.setCreationDate(rs.getTimestamp("creationDate"));
	    boatPositionHistory.setHeading(rs.getInt("heading"));
	    boatPositionHistory.setSail(SailEnum.fromString(rs.getString("sail")));
	    boatPositionHistory.setLatitude(rs.getDouble("latitude"));
	    boatPositionHistory.setLongitude(rs.getDouble("longitude"));
	    boatPositionHistory.setHasChangedCase(rs.getBoolean("hasChangedCase"));
	    return boatPositionHistory;
	  }
	  
	  
}
