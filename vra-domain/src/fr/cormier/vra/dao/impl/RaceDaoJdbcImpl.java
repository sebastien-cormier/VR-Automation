package fr.cormier.vra.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.cormier.domain.db.Race;
import fr.cormier.vra.dao.IRaceDao;
import fr.cormier.vra.dao.mapper.RaceRowMapper;

/**
 * DAO class for Schedule table
 * 
 * A UserRace describe the races play by a user and the activated mode for each race
 * 
 * @author Sebastien Cormier
 *
 */
public class RaceDaoJdbcImpl extends JdbcDaoSupport implements IRaceDao {
	
	/**
	 * Persist a Race entity
	 * 
	 * @param userRace
	 *            entity
	 * @return primary key
	 */
	public int create(final Race race) {

		final String sql = "INSERT INTO "+TABLENAME+" (" + COLUMNS + ") VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, 0);
				ps.setString(2, race.getName());
				if( race.getStartDate()!=null ) {
					ps.setDate(3, new Date(race.getStartDate().getTime()));
				} else {
					ps.setDate(3, null);
				}
				ps.setString(4, race.getStatus().getValue());
				return ps;
			}
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}

	/**
	 * Retrieve of Race entity
	 * 
	 * @return the list of Race
	 */
	public List<Race> selectAll() {
		return getJdbcTemplate().query("select " + COLUMNS + " FROM Race",
				new RaceRowMapper());
	}

}
