package fr.cormier.vra.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import fr.cormier.domain.db.UserRace;
import fr.cormier.vra.dao.IUserRaceDao;
import fr.cormier.vra.dao.mapper.UserRaceRowMapper;

/**
 * DAO for UserRace table
 * 
 * @author Sebastien Cormier
 * 
 */
public class UserRaceDaoJdbcImpl extends JdbcDaoSupport implements IUserRaceDao {

	/**
	 * Persist a UserRace entity
	 * 
	 * @param userRace
	 *            entity
	 * @return primary key
	 */
	public int create(final UserRace userRace) {

		final String sql = "INSERT INTO UserRace (" + COLUMNS
				+ ") VALUES(?,?,?,?)";
		return getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, userRace.getVrUserId());
				ps.setInt(2, userRace.getRaceId());
				ps.setString(3, userRace.getMode());
				ps.setString(4, userRace.getUserService());
				return ps;
			}
		});
	}

	/**
	 * Retrieve all UserRace data
	 * 
	 * @return List of UserRace entity
	 */
	public List<UserRace> selectAll() {
		String sql = "select " + COLUMNS + " FROM UserRace";
		return getJdbcTemplate().query(sql,
				new UserRaceRowMapper());
	}

	/**
	 * Retrieve a sing UserRace entity by primary key
	 * 
	 * @param vrUserId
	 *            VR user id
	 * @param raceId
	 *            race id
	 * @return the UserRace Entity
	 */
	public UserRace selectByKey(int vrUserId, int raceId) {
		String sql = "select " + COLUMNS
				+ " FROM UserRace WHERE vrUserId = ? AND raceId = ?";
		try {
			return getJdbcTemplate().queryForObject(sql,
					new Object[] { vrUserId, raceId }, new UserRaceRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Retrieve all UserRace entity in "Schedule" mode
	 * 
	 * @return list of UserRace entity
	 */
	public List<UserRace> selectAllSchedule() {
		String sql = "select " + COLUMNS + " FROM UserRace WHERE mode = '"
				+ UserRace.MODE_SCHEDULE + "'";
		return getJdbcTemplate().query(sql, new UserRaceRowMapper());
	}

	@Override
	public List<UserRace> selectAllZezoAutoRouting() {
		String sql = "select " + COLUMNS + " FROM UserRace WHERE mode = '"
				+ UserRace.MODE_ZEZO_AUTO_ROUTING + "'";
		return getJdbcTemplate().query(sql, new UserRaceRowMapper());
	}

}
