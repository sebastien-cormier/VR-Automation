package fr.cormier.vra.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.cormier.domain.db.BoatPositionHistory;
import fr.cormier.vra.dao.IBoatPositionHistoryDao;
import fr.cormier.vra.dao.mapper.BoatPositionHistoryRowMapper;

/**
 * DAO class for Boat Position History table
 * 
 * All entries specified location and routing at a specified time
 * 
 * @author Sebastien Cormier
 * 
 */
public class BoatPositionHistoryDaoJdbcImpl extends JdbcDaoSupport implements IBoatPositionHistoryDao {

	/**
	 * Persist a BoatPositionHistory entity
	 * 
	 * @param command
	 *            entity
	 * @return primary key
	 */
	public int create(final BoatPositionHistory boatPositionHistory) {
		final String sql = "INSERT INTO "+TABLENAME+" (" + COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, 0);
				ps.setInt(2, boatPositionHistory.getVrUserId());
				ps.setInt(3, boatPositionHistory.getRaceId());
				ps.setDate(4, new Date(boatPositionHistory.getCreationDate().getTime()));
				ps.setString(5, boatPositionHistory.getSail().name());
				ps.setInt(6, boatPositionHistory.getHeading());
				ps.setDouble(7, boatPositionHistory.getLatitude());
				ps.setDouble(8, boatPositionHistory.getLongitude());
				ps.setInt(9, boatPositionHistory.isHasChangedCase()?1:0);
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	public BoatPositionHistory selectLastPosition(int vrUserId,
			int raceId) {
		List<BoatPositionHistory> boatPositions = getJdbcTemplate()
				.query("select "
						+ COLUMNS
						+ " FROM BoatPositionHistory WHERE VrUserId = ? AND RaceId= ? ORDER BY CreationDate DESC LIMIT 1",
						new Object[] { vrUserId, raceId  },
						new BoatPositionHistoryRowMapper());
		if (boatPositions == null || boatPositions.size() == 0) {
			return null;
		} else {
			return boatPositions.get(0);
		}
	}



}
