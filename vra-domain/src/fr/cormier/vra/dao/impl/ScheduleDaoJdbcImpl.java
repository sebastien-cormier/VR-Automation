package fr.cormier.vra.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.cormier.domain.db.Schedule;
import fr.cormier.domain.db.ScheduleStatusEnum;
import fr.cormier.vra.dao.IScheduleDao;
import fr.cormier.vra.dao.mapper.ScheduleRowMapper;

/**
 * DAO class for Schedule table
 * 
 * A schedule is a scheduled task. Schedule is use when user race mode is set to
 * 'SCHEDULE'
 * 
 * @author Sebastien Cormier
 * 
 */
public class ScheduleDaoJdbcImpl extends JdbcDaoSupport implements IScheduleDao {

	/**
	 * Persist a Schedule entity
	 * 
	 * @param userRace
	 *            entity
	 * @return primary key
	 */
	public int create(final Schedule schedule) {

		final String sql = "INSERT INTO Schedule (" + COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, 0);
				ps.setInt(2, schedule.getVrUserId());
				ps.setInt(3, schedule.getRaceId());
				ps.setInt(4, schedule.getHeading());
				ps.setInt(5, schedule.getSail().getValue());
				ps.setTimestamp(6, new Timestamp(schedule.getStartDate().getTime()));
				ps.setString(7, String.valueOf(schedule.getStatus()));
				ps.setString(8, String.valueOf(schedule.getTestStatus()));
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	/**
	 * return the list of "pending" schedule that have to be execute
	 * 
	 * @param vrUserId
	 *            vitrual regatta user id
	 * @param raceId
	 *            race id
	 * 
	 * @return the list of schedule
	 */
	public List<Schedule> selectScheduleToRotate(int vrUserId, int raceId) {
		String sql = "select "
				+ COLUMNS
				+ " FROM Schedule WHERE vrUserId = ? AND RaceId = ? AND status = ? AND startDate<now()";
		return getJdbcTemplate().query(sql, new Object[] { vrUserId, raceId,
				ScheduleStatusEnum.PENDING.getValue() }, new ScheduleRowMapper());
	}

	/**
	 * Set a list of schedule as status FINISH
	 * 
	 * @param scheduleIds
	 *            the list of schedule ids
	 * @return number of schedule updated
	 */
	public int setFinishStatus(Set<Integer> scheduleIds) {
		String sql = "UPDATE Schedule SET Status='" + ScheduleStatusEnum.FINISH
				+ "' WHERE ScheduleId IN ( :ids )";
		NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(
				getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", scheduleIds);
		return namedParamJdbcTemplate.update(sql, parameters);

	}

	/**
	 * Update modification on an entity
	 * 
	 * @param schedule
	 * @return
	 */
	public int update(Schedule schedule) {

		return getJdbcTemplate()
				.update("UPDATE Schedule SET vrUserId=?, raceId=?, heading=?, sail=?, startDate=?, status=? WHERE scheduleId=?",
						new Object[] { schedule.getVrUserId(),
								schedule.getRaceId(), schedule.getHeading(),
								schedule.getSail().getStringValue(),
								schedule.getStartDate(), schedule.getStatus().getValue(),
								schedule.getScheduleId() });

	}

	/**
	 * Retrieve preceding schedule from a new one
	 * 
	 * @param vrUserId
	 *            virtual regatta user id
	 * @param raceId
	 *            race id
	 * @param startDate
	 *            start date of the new schedule
	 * @return the preceding schedule
	 */
	public Schedule retrieveScheduleBefore(int vrUserId, int raceId,
			java.util.Date startDate) {
		try {
			return getJdbcTemplate()
					.queryForObject(
							"select "
									+ COLUMNS
									+ " FROM Schedule WHERE startDate < ? AND Status=? AND vrUserId=? AND raceId=?",
							new Object[] { startDate,
									ScheduleStatusEnum.CURRENT.getValue(), vrUserId,
									raceId }, new ScheduleRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Retrieve all current schedule
	 * 
	 * @param vrUserId
	 *            virtual regatta user id
	 * @param raceId
	 *            race id
	 * @return
	 */
	public List<Schedule> selectCurrentSchedule(int vrUserId, int raceId) {
		String query = "select "
				+ COLUMNS
				+ " FROM Schedule WHERE vrUserId = ? AND raceId = ? AND status = ?";
		
		return getJdbcTemplate()
				.query(query,
						new Object[] { vrUserId, raceId,
								String.valueOf(ScheduleStatusEnum.CURRENT.getValue()) },
						new ScheduleRowMapper());
	}

}
