package fr.cormier.vra.dao;

import java.util.List;
import java.util.Set;

import fr.cormier.domain.db.Schedule;

/**
 * DAO class for Schedule table
 * 
 * A schedule is a scheduled task. Schedule is use when user race mode is set to
 * 'SCHEDULE'
 * 
 * @author Sebastien Cormier
 * 
 */
public interface IScheduleDao {

	public static final String COLUMNS = "scheduleId, vrUserId, raceId, heading, sail, startDate, status, testStatus";

	public static final String TABLENAME = "Schedule";
	
	public int create(final Schedule schedule);
	
	public List<Schedule> selectScheduleToRotate(int vrUserId, int raceId);
	
	public int setFinishStatus(Set<Integer> scheduleIds);
	
	public int update(Schedule schedule);

	public Schedule retrieveScheduleBefore(int vrUserId, int raceId,
			java.util.Date startDate);
	
	public List<Schedule> selectCurrentSchedule(int vrUserId, int raceId);
}
