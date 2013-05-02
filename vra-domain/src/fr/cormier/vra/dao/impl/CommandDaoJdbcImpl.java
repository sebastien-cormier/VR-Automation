package fr.cormier.vra.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.vra.dao.ICommandDao;
import fr.cormier.vra.dao.mapper.CommandRowMapper;

/**
 * DAO class for Command table
 * 
 * A command is a description how to launch a command to retrieve/modify data on
 * Virtual Regatta
 * 
 * @author Sebastien Cormier
 * 
 */
public class CommandDaoJdbcImpl extends JdbcDaoSupport implements ICommandDao {

	/**
	 * Persist a Command entity
	 * 
	 * @param command
	 *            entity
	 * @return primary key
	 */
	public int create(final Command command) {

		final String sql = "INSERT INTO Command (" + COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, 0);
				ps.setString(2, String.valueOf(command.getCommandType()));
				ps.setInt(3, command.getVrUserId());
				ps.setInt(4, command.getRaceId());
				ps.setString(5, command.getValue());
				ps.setString(6, command.getR());
				ps.setString(7, command.getChecksum());
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	/**
	 * Retrieve a single command
	 * @param userId
	 * @param raceId
	 * @param commandType
	 * @param value
	 * @return command entity
	 */
	public Command select(int userId, int raceId, CommandTypeEnum commandType,
			String value) {
		List<Command> commands = getJdbcTemplate()
				.query("select "
						+ COLUMNS
						+ " FROM Command WHERE VrUserId = ? AND RaceId= ? AND CommandType=? and value=? LIMIT 1",
						new Object[] { userId, raceId,
								String.valueOf(commandType), value },
						new CommandRowMapper());
		if (commands == null || commands.size() == 0) {
			return null;
		} else {
			return commands.get(0);
		}
	}

	/**
	 * Retrieve a single command by primary key
	 * @param commandId
	 * @return command entity
	 */
	public Command selectByKey(int commandId) {
		try {
			return getJdbcTemplate().queryForObject(
					"select " + COLUMNS + " FROM Command WHERE CommandId = ?",
					new Object[] { commandId }, new CommandRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Update checksul and r values
	 * @param command
	 * @return
	 */
	public int updateChecksumAndR(Command command) {
		return getJdbcTemplate().update(
				"UPDATE Command SET checksum=?, r=? WHERE commandId=?",
				new Object[] { command.getChecksum(), command.getR(),
						command.getCommandId() });

	}

	/**
	 * Retrieve command
	 * @param vrUserId
	 * @param raceId
	 * @param commandTypeHeading
	 * @param heading
	 * @return
	 */
	public Command findByValues(int vrUserId, int raceId,
			String commandTypeHeading, String heading) {
		try {
			return getJdbcTemplate()
					.queryForObject(
							"select "
									+ COLUMNS
									+ " FROM Command WHERE vrUserId = ? AND raceId = ? AND commandType = ? AND value = ?",
							new Object[] { vrUserId, raceId,
									commandTypeHeading, heading },
							new CommandRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * Retrieve all comands by user / race ids
	 * @param vrUserId
	 * @param raceId
	 * @return list of commands
	 */
	public List<Command> select(int vrUserId, int raceId) {
		List<Command> commands = getJdbcTemplate().query(
				"select " + COLUMNS
						+ " FROM Command WHERE VrUserId = ? AND RaceId = ?",
				new Object[] { vrUserId, raceId }, new CommandRowMapper());
		if (commands == null || commands.size() == 0) {
			return null;
		} else {
			return commands;
		}
	}

}
