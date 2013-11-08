package fr.cormier.vra.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.cormier.domain.SailEnum;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.domain.db.RaceStatusEnum;
import fr.cormier.domain.db.ScheduleStatusEnum;
import fr.cormier.domain.db.ScheduleTestStatusEnum;
import fr.cormier.vra.dao.IBoatPositionHistoryDao;
import fr.cormier.vra.dao.impl.CommandDaoJdbcImpl;
import fr.cormier.vra.dao.impl.RaceDaoJdbcImpl;
import fr.cormier.vra.dao.impl.ScheduleDaoJdbcImpl;
import fr.cormier.vra.dao.impl.UserRaceDaoJdbcImpl;

public class TestUtilDaoJdbc extends JdbcDaoSupport {

	private static final int MAX_ID = 100000;
	private static Map<String, String> SQL_CREATE_TABLE = new HashMap<String, String>();;
	
	static {
		SQL_CREATE_TABLE.put(CommandDaoJdbcImpl.TABLENAME, "" +
				"CREATE TABLE `Command` ("+
				"  `commandId` int(11) unsigned NOT NULL AUTO_INCREMENT, "+
				"  `commandType` enum('HEADING','SAIL') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'HEADING', "+
				"  `vrUserId` int(11) unsigned NOT NULL, "+
				"  `raceId` int(11) NOT NULL, "+
				"  `value` varchar(64) COLLATE utf8_swedish_ci NOT NULL, "+
				"  `r` varchar(255) COLLATE utf8_swedish_ci NOT NULL, "+
				"  `checksum` varchar(255) COLLATE utf8_swedish_ci NOT NULL, "+
				"  PRIMARY KEY (`commandId`) "+
				") ENGINE=InnoDB AUTO_INCREMENT=624 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci");
		
		SQL_CREATE_TABLE.put(RaceDaoJdbcImpl.TABLENAME, "" +
				"CREATE TABLE `Race` ( "+
				"  `raceId` int(6) unsigned NOT NULL AUTO_INCREMENT, "+
				"  `name` varchar(255) COLLATE utf8_swedish_ci NOT NULL, "+
				"  `startDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "+
				"  `status` enum('CLOSE','OPEN','PENDING') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'OPEN', "+
				"  PRIMARY KEY (`raceId`) "+
				") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci");

		SQL_CREATE_TABLE.put(ScheduleDaoJdbcImpl.TABLENAME, "" +
				"CREATE TABLE `Schedule` ("+
				"  `scheduleId` int(11) unsigned NOT NULL AUTO_INCREMENT,"+
				"  `vrUserId` int(11) NOT NULL,"+
				"  `raceId` int(11) NOT NULL,"+
				"  `heading` int(3) NOT NULL,"+
				"  `sail` enum('JIB','SPI') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'SPI',"+
				"  `startDate` datetime NOT NULL,"+
				"  `status` enum('FINISH','CURRENT','PENDING') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'PENDING',"+
				"  `testStatus` enum('TO_TEST','FAIL_1','FAIL_2','FAIL_3','CHECKED') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'TO_TEST',"+
				"  PRIMARY KEY (`scheduleId`)"+
				") ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;");

		SQL_CREATE_TABLE.put(UserRaceDaoJdbcImpl.TABLENAME, "" +
				"CREATE TABLE `UserRace` ( "+
				"  `vrUserId` int(11) NOT NULL, "+
				"  `raceId` int(11) NOT NULL, "+
				"  `mode` enum('SCHEDULE','WAYPOINTS','SPEEDREG') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'SCHEDULE', "+
				"  `userService` varchar(512) COLLATE utf8_swedish_ci DEFAULT NULL, "+
				"  PRIMARY KEY (`vrUserId`,`raceId`) "+
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;");

		SQL_CREATE_TABLE.put(BoatPositionHistoryDaoJdbcImpl.TABLENAME, "" +
				"CREATE TABLE `BoatPositionHistory` ("+
				"		  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,"+
				"		  `vrUserId` int(11) NOT NULL,"+
				"		  `raceId` int(11) NOT NULL,"+
				"		  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"+
				"		  `sail` enum('JIB','SPI') COLLATE utf8_swedish_ci NOT NULL,"+
				"		  `heading` tinyint(3) NOT NULL,"+
				"		  `latitude` double NOT NULL,"+
				"		  `longitude` double NOT NULL,"+
				"		  `hasChangedCase` tinyint(1) NOT NULL DEFAULT '0',"+
				"		  PRIMARY KEY (`id`)"+
				"		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci");


	}
	public TestUtilDaoJdbc() {
		super();
	}
	
	public void dropTable(String tableName) {
		String sql = "DROP TABLE IF EXISTS `"+tableName+"`";
		getJdbcTemplate().update(sql);
	}

	public void createTable(String tableName) {
		getJdbcTemplate().update(SQL_CREATE_TABLE.get(tableName));
	}
	
	public int createCommand(final CommandTypeEnum commandType, final int vrUserId, final int raceId, final String value, final String r, final String checksum) {
		final String sql = "INSERT INTO Command (" + CommandDaoJdbcImpl.COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ps.setString(2, String.valueOf(commandType));
				ps.setInt(3, vrUserId);
				ps.setInt(4, raceId);
				ps.setString(5, value);
				ps.setString(6, r);
				ps.setString(7, checksum);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public int createRace(final String name,
			final Date startDate, final RaceStatusEnum status) {
		final String sql = "INSERT INTO "+RaceDaoJdbcImpl.TABLENAME+" (" + RaceDaoJdbcImpl.COLUMNS + ") VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ps.setString(2, name);
				if( startDate!=null ) {
					ps.setDate(3, new java.sql.Date(startDate.getTime()));
				} else {
					ps.setDate(3, null);
				}
				ps.setString(4, status.getValue());
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	public int createSchedule(final int vrUserId, final int raceId,
			final int heading, final SailEnum sail, final Date startDate,
			final ScheduleStatusEnum status, final ScheduleTestStatusEnum testStatus) {
		final String sql = "INSERT INTO Schedule (" + ScheduleDaoJdbcImpl.COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ps.setInt(2, vrUserId);
				ps.setInt(3, raceId);
				ps.setInt(4, heading);
				ps.setInt(5, sail.getValue());
				ps.setTimestamp(6, new java.sql.Timestamp(startDate.getTime()));
				ps.setString(7, String.valueOf(status));
				ps.setString(8, String.valueOf(testStatus));
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	public void createUserRace(final int vrUserId, final int raceId,
			final String mode, final String userService) {
		final String sql = "INSERT INTO UserRace (" + UserRaceDaoJdbcImpl.COLUMNS
				+ ") VALUES(?,?,?,?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, vrUserId);
				ps.setInt(2, raceId);
				ps.setString(3, mode);
				ps.setString(4, userService);
				return ps;
			}
		});

	}
	
	public int randId() {
		return (int)(Math.random()*MAX_ID);
	}

	public int createBoatPositionHistory(final int testVrUserId, final int testRaceId,
			final Date testDateCreation, final int testHeading, final SailEnum testSail,
			final double testLatitude, final double testLongitude, final boolean testHaschangecase) {
		
		final String sql = "INSERT INTO "+IBoatPositionHistoryDao.TABLENAME+" (" + IBoatPositionHistoryDao.COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ps.setInt(2, testVrUserId);
				ps.setInt(3, testRaceId);
				ps.setTimestamp(4, new java.sql.Timestamp(testDateCreation.getTime()));
				ps.setString(5, testSail.name());
				ps.setInt(6, testHeading);
				ps.setDouble(7, testLatitude);
				ps.setDouble(8, testLongitude);
				ps.setInt(9, testHaschangecase?1:0);
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}
}
